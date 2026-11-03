package com.bibliotech.rental.scheduler;

import com.bibliotech.rental.client.NotificationClient;
import com.bibliotech.rental.dto.NotificationRequest;
import com.bibliotech.rental.entity.Rental;
import com.bibliotech.rental.entity.RentalStatus;
import com.bibliotech.rental.repository.RentalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Flips ACTIVE rentals whose due date has passed to OVERDUE and notifies the
 * borrower. Runs automatically - no manual trigger required.
 */
@Component
public class OverdueScheduler {

    private static final Logger log = LoggerFactory.getLogger(OverdueScheduler.class);

    private final RentalRepository rentalRepository;
    private final NotificationClient notificationClient;

    public OverdueScheduler(RentalRepository rentalRepository, NotificationClient notificationClient) {
        this.rentalRepository = rentalRepository;
        this.notificationClient = notificationClient;
    }

    /** Daily sweep at 01:00. */
    @Scheduled(cron = "0 0 1 * * *")
    public void dailySweep() {
        detectOverdueRentals();
    }

    /** One extra sweep shortly after startup so the demo data is correct immediately. */
    @Scheduled(initialDelay = 20000, fixedDelay = 86400000)
    public void startupSweep() {
        detectOverdueRentals();
    }

    public void detectOverdueRentals() {
        LocalDate today = LocalDate.now();
        // strictly before today => a book due today is not yet overdue
        List<Rental> overdueCandidates =
                rentalRepository.findByStatusAndDueDateBefore(RentalStatus.ACTIVE, today);

        for (Rental rental : overdueCandidates) {
            rental.setStatus(RentalStatus.OVERDUE);
            rentalRepository.save(rental);

            long lateDays = ChronoUnit.DAYS.between(rental.getDueDate(), today);
            long fine = lateDays * 10;
            String message = "Your book '" + rental.getBookTitle() + "' is overdue by "
                    + lateDays + " day(s). Current fine: Rs." + fine + ".";

            try {
                notificationClient.createNotification(
                        new NotificationRequest(rental.getUserId(), message, "OVERDUE"));
            } catch (Exception e) {
                // Graceful degradation: a notification failure must not roll back the status change
                log.warn("Could not send overdue notification for rental {}: {}", rental.getId(), e.getMessage());
            }
        }

        if (!overdueCandidates.isEmpty()) {
            log.info("Marked {} rental(s) as OVERDUE", overdueCandidates.size());
        }
    }
}
