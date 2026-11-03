package com.bibliotech.book.controller;

import com.bibliotech.book.dto.BranchDto;
import com.bibliotech.book.entity.Branch;
import com.bibliotech.book.repository.BranchRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books/branches")
public class BranchController {
    private final BranchRepository branchRepository;
    public BranchController(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }
    @GetMapping
    public ResponseEntity<List<BranchDto>> getBranches() {
        return ResponseEntity.ok(branchRepository.findAll().stream()
                .map(b -> new BranchDto(b.getId(), b.getName(), b.getLocation()))
                .collect(Collectors.toList()));
    }
}
