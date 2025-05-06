insert into currencies (Code, FullName, Sign)
values ('AUD', 'Australian dollar', 'A$'),
       ('RUB', 'Russian ruble', '₽'),
       ('TRY', 'Turkish lira', '₺'),
       ('GBP', 'Pounds sterling', '£'),
       ('ILS', 'Israeli shekel', '₪'),
       ('EUR', 'Euro', '€'),
       ('USD', 'United States dollar', '$');

insert into exchangeRates (BaseCurrencyId, TargetCurrencyId, Rate)
values (7, 2, 96),
       (7, 6, 0.96),
       (4, 7, 1.24);