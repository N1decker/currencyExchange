create table if not exists currencies
(
    ID       integer primary key,
    Code     varchar not null unique,
    FullName varchar not null,
    Sign     varchar not null
);

create table if not exists exchangeRates
(
    ID                  integer primary key,
    BaseCurrencyId      integer not null references currencies(ID) on delete cascade,
    TargetCurrencyId    integer not null references currencies(ID) on delete cascade,
    Rate                decimal(6) not null,
    constraint unique_baseCur_targetCur unique (BaseCurrencyId, TargetCurrencyId)
);