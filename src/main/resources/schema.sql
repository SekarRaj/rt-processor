CREATE SEQUENCE IF NOT EXISTS('deals_sequence')

CREATE TABLE IF NOT EXISTS deals (
  id bigint DEFAULT next_val('deals_sequence') primary key,
  code varchar,
  description varchar,
  validFrom timestamp,
  validTo timestamp,
  createdOn timestamp,
  updatedOn timestamp
);