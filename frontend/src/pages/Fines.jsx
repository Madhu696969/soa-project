import React, { useEffect, useState } from 'react';
import { fineService } from '../services/fineService';
import Loader from '../components/Loader';
import ErrorMessage from '../components/ErrorMessage';
import StatusBadge from '../components/StatusBadge';
import { extractErrorMessage } from '../utils/helpers';

export default function Fines() {
  const [fines, setFines] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [payingId, setPayingId] = useState(null);

  const load = async () => {
    setLoading(true);
    try {
      const res = await fineService.myFines();
      setFines(res.data);
    } catch (err) {
      setError(extractErrorMessage(err, 'Could not load fines'));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
  }, []);

  const handlePay = async (fineId) => {
    setPayingId(fineId);
    try {
      await fineService.pay(fineId);
      await load();
    } catch (err) {
      setError(extractErrorMessage(err, 'Could not process payment'));
    } finally {
      setPayingId(null);
    }
  };

  return (
    <div className="page">
      <h1>My Fines</h1>
      <ErrorMessage message={error} />
      {loading ? (
        <Loader />
      ) : (
        <table className="data-table">
          <thead>
            <tr>
              <th>Rental Id</th>
              <th>Late Days</th>
              <th>Amount</th>
              <th>Status</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {fines.map((f) => (
              <tr key={f.id}>
                <td>#{f.rentalId}</td>
                <td>{f.lateDays}</td>
                <td>₹{f.amount}</td>
                <td><StatusBadge status={f.status} /></td>
                <td>
                  {f.status === 'PENDING' ? (
                    <button className="btn btn-primary" disabled={payingId === f.id} onClick={() => handlePay(f.id)}>
                      {payingId === f.id ? 'Processing...' : 'Pay Now'}
                    </button>
                  ) : (
                    <span className="muted">-</span>
                  )}
                </td>
              </tr>
            ))}
            {fines.length === 0 && (
              <tr><td colSpan="5">No fines on your account. 🎉</td></tr>
            )}
          </tbody>
        </table>
      )}
    </div>
  );
}
