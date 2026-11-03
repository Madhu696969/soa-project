import React from 'react';

const STATUS_CLASSES = {
  AVAILABLE: 'badge badge-success',
  BORROWED: 'badge badge-warning',
  LOST: 'badge badge-danger',
  DAMAGED: 'badge badge-danger',
  ACTIVE: 'badge badge-info',
  RETURNED: 'badge badge-success',
  OVERDUE: 'badge badge-danger',
  PENDING: 'badge badge-warning',
  PAID: 'badge badge-success'
};

export default function StatusBadge({ status }) {
  return <span className={STATUS_CLASSES[status] || 'badge'}>{status}</span>;
}
