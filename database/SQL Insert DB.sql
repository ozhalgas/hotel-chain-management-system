Insert into mydb.hotel values (0, 'A', 'Spain', 'Barcelona', 'Messi 10');
Insert into mydb.hotel values (1, 'B', 'USA', 'New York', 'Wall street 23');

Insert into mydb.phone_number values ('232323', 0);
Insert into mydb.phone_number values ('222222', 0);
Insert into mydb.phone_number values ('542132', 1);
Insert into mydb.phone_number values ('542133', 1);

Insert into mydb.category values ('None', 0, 0);
Insert into mydb.category values ('Gold', 0.3, 0);
Insert into mydb.category values ('Silver', 0.2, 0);
Insert into mydb.category values ('Bronze', 0.1, 0);

Insert into mydb.category values ('None', 0, 1);
Insert into mydb.category values ('Gold', 0.3, 1);
Insert into mydb.category values ('Silver', 0.2, 1);
Insert into mydb.category values ('Bronze', 0.1, 1);

Insert into mydb.room_type values ('Single', 26.0, 2, 2, 2, 0);
Insert into mydb.room_type values ('Double', 32.0, 4, 2, 2, 0);

Insert into mydb.room_type values ('Single', 28.0, 2, 2, 2, 1);
Insert into mydb.room_type values ('Double', 34.0, 4, 2, 2, 1);

Insert into mydb.room values ('310', 1, 1, 0, 0, 'Single', 0);
Insert into mydb.room values ('311', 1, 1, 0, 0, 'Single', 0);
Insert into mydb.room values ('312', 1, 1, 0, 0, 'Double', 0);
Insert into mydb.room values ('313', 1, 1, 0, 0, 'Double', 0);

Insert into mydb.room values ('310', 1, 1, 0, 0, 'Single', 1);
Insert into mydb.room values ('311', 1, 1, 0, 0, 'Single', 1);
Insert into mydb.room values ('312', 1, 1, 0, 0, 'Double', 1);
Insert into mydb.room values ('313', 1, 1, 0, 0, 'Double', 1);

Insert into mydb.time_period values ('M', 'winter', '2020-12-01', '2021-02-28');
Insert into mydb.time_period values ('T', 'winter', '2020-12-01', '2021-02-28');
Insert into mydb.time_period values ('W', 'winter', '2020-12-01', '2021-02-28');
Insert into mydb.time_period values ('R', 'winter', '2020-12-01', '2021-02-28');
Insert into mydb.time_period values ('F', 'winter', '2020-12-01', '2021-02-28');
Insert into mydb.time_period values ('S', 'winter', '2020-12-01', '2021-02-28');
Insert into mydb.time_period values ('H', 'winter', '2020-12-01', '2021-02-28');

Insert into mydb.time_period values ('M', 'regular', '2020-01-01',  '2021-12-31');
Insert into mydb.time_period values ('T', 'regular', '2020-01-01',  '2021-12-31');
Insert into mydb.time_period values ('W', 'regular', '2020-01-01',  '2021-12-31');
Insert into mydb.time_period values ('R', 'regular', '2020-01-01',  '2021-12-31');
Insert into mydb.time_period values ('F', 'regular', '2020-01-01',  '2021-12-31');
Insert into mydb.time_period values ('S', 'regular', '2020-01-01',  '2021-12-31');
Insert into mydb.time_period values ('H', 'regular', '2020-01-01',  '2021-12-31');

Insert into mydb.operates_during values (0, 'M', 'winter','2020-12-01',  '2021-02-28');
Insert into mydb.operates_during values (0, 'T', 'winter','2020-12-01',  '2021-02-28');
Insert into mydb.operates_during values (0, 'W', 'winter','2020-12-01',  '2021-02-28');
Insert into mydb.operates_during values (0, 'R', 'winter','2020-12-01',  '2021-02-28');
Insert into mydb.operates_during values (0, 'F', 'winter','2020-12-01',  '2021-02-28');
Insert into mydb.operates_during values (0, 'S', 'winter','2020-12-01',  '2021-02-28');
Insert into mydb.operates_during values (0, 'H', 'winter','2020-12-01',  '2021-02-28');

Insert into mydb.operates_during values (1, 'M', 'winter','2020-12-01', '2021-02-28');
Insert into mydb.operates_during values (1, 'T', 'winter','2020-12-01', '2021-02-28');
Insert into mydb.operates_during values (1, 'W', 'winter','2020-12-01', '2021-02-28');
Insert into mydb.operates_during values (1, 'R', 'winter','2020-12-01', '2021-02-28');
Insert into mydb.operates_during values (1, 'F', 'winter','2020-12-01', '2021-02-28');
Insert into mydb.operates_during values (1, 'S', 'winter','2020-12-01', '2021-02-28');
Insert into mydb.operates_during values (1, 'H', 'winter','2020-12-01', '2021-02-28');

Insert into mydb.operates_during values (0, 'M', 'regular','2020-01-01', '2021-12-31');
Insert into mydb.operates_during values (0, 'T', 'regular','2020-01-01', '2021-12-31');
Insert into mydb.operates_during values (0, 'W', 'regular','2020-01-01', '2021-12-31');
Insert into mydb.operates_during values (0, 'R', 'regular','2020-01-01', '2021-12-31');
Insert into mydb.operates_during values (0, 'F', 'regular','2020-01-01', '2021-12-31');
Insert into mydb.operates_during values (0, 'S', 'regular','2020-01-01', '2021-12-31');
Insert into mydb.operates_during values (0, 'H', 'regular','2020-01-01', '2021-12-31');

Insert into mydb.operates_during values (1, 'M', 'regular','2020-01-01', '2021-12-31');
Insert into mydb.operates_during values (1, 'T', 'regular','2020-01-01', '2021-12-31');
Insert into mydb.operates_during values (1, 'W', 'regular','2020-01-01', '2021-12-31');
Insert into mydb.operates_during values (1, 'R', 'regular','2020-01-01', '2021-12-31');
Insert into mydb.operates_during values (1, 'F', 'regular','2020-01-01', '2021-12-31');
Insert into mydb.operates_during values (1, 'S', 'regular','2020-01-01', '2021-12-31');
Insert into mydb.operates_during values (1, 'H', 'regular','2020-01-01', '2021-12-31');

-- Initial prices for roomtypes
-- For Hotel with ID = 0
Insert into mydb.initial_price values ('Single', 0, 'M', 'regular', 60.0, '2020-01-01', '2021-12-31');
Insert into mydb.initial_price values ('Single', 0, 'T', 'regular', 60.0, '2020-01-01', '2021-12-31');
Insert into mydb.initial_price values ('Single', 0, 'W', 'regular', 60.0, '2020-01-01', '2021-12-31');
Insert into mydb.initial_price values ('Single', 0, 'R', 'regular', 60.0, '2020-01-01', '2021-12-31');
Insert into mydb.initial_price values ('Single', 0, 'F', 'regular', 65.0, '2020-01-01', '2021-12-31');
Insert into mydb.initial_price values ('Single', 0, 'S', 'regular', 70.0, '2020-01-01', '2021-12-31');
Insert into mydb.initial_price values ('Single', 0, 'H', 'regular', 70.0, '2020-01-01', '2021-12-31');

Insert into mydb.initial_price values ('Double', 0, 'M', 'regular', 80.0, '2020-01-01', '2021-12-31');
Insert into mydb.initial_price values ('Double', 0, 'T', 'regular', 80.0, '2020-01-01', '2021-12-31');
Insert into mydb.initial_price values ('Double', 0, 'W', 'regular', 80.0,'2020-01-01', '2021-12-31');
Insert into mydb.initial_price values ('Double', 0, 'R', 'regular', 80.0,'2020-01-01', '2021-12-31');
Insert into mydb.initial_price values ('Double', 0, 'F', 'regular', 85.0,'2020-01-01', '2021-12-31');
Insert into mydb.initial_price values ('Double', 0, 'S', 'regular', 90.0,'2020-01-01', '2021-12-31');
Insert into mydb.initial_price values ('Double', 0, 'H', 'regular', 90.0,'2020-01-01', '2021-12-31');

Insert into mydb.initial_price values ('Single', 0, 'M', 'winter', 90.0, '2020-12-01', '2021-02-28');
Insert into mydb.initial_price values ('Single', 0, 'T', 'winter', 90.0, '2020-12-01', '2021-02-28');
Insert into mydb.initial_price values ('Single', 0, 'W', 'winter', 90.0, '2020-12-01', '2021-02-28');
Insert into mydb.initial_price values ('Single', 0, 'R', 'winter', 90.0, '2020-12-01', '2021-02-28' );
Insert into mydb.initial_price values ('Single', 0, 'F', 'winter', 95.0, '2020-12-01', '2021-02-28');
Insert into mydb.initial_price values ('Single', 0, 'S', 'winter', 105.0, '2020-12-01', '2021-02-28');
Insert into mydb.initial_price values ('Single', 0, 'H', 'winter', 105.0, '2020-12-01', '2021-02-28');

Insert into mydb.initial_price values ('Double', 0, 'M', 'winter', 120.0, '2020-12-01', '2021-02-28');
Insert into mydb.initial_price values ('Double', 0, 'T', 'winter', 120.0, '2020-12-01', '2021-02-28');
Insert into mydb.initial_price values ('Double', 0, 'W', 'winter', 120.0, '2020-12-01', '2021-02-28');
Insert into mydb.initial_price values ('Double', 0, 'R', 'winter', 120.0, '2020-12-01', '2021-02-28');
Insert into mydb.initial_price values ('Double', 0, 'F', 'winter', 125.0, '2020-12-01', '2021-02-28');
Insert into mydb.initial_price values ('Double', 0, 'S', 'winter', 135.0, '2020-12-01', '2021-02-28');
Insert into mydb.initial_price values ('Double', 0, 'H', 'winter', 135.0, '2020-12-01', '2021-02-28');

-- For Hotel with ID = 1
Insert into mydb.initial_price values ('Single', 1, 'M', 'regular', 60.0, '2020-01-01', '2021-12-31');
Insert into mydb.initial_price values ('Single', 1, 'T', 'regular', 60.0, '2020-01-01', '2021-12-31');
Insert into mydb.initial_price values ('Single', 1, 'W', 'regular', 60.0, '2020-01-01', '2021-12-31');
Insert into mydb.initial_price values ('Single', 1, 'R', 'regular', 60.0, '2020-01-01', '2021-12-31');
Insert into mydb.initial_price values ('Single', 1, 'F', 'regular', 65.0, '2020-01-01', '2021-12-31');
Insert into mydb.initial_price values ('Single', 1, 'S', 'regular', 70.0, '2020-01-01', '2021-12-31');
Insert into mydb.initial_price values ('Single', 1, 'H', 'regular', 70.0, '2020-01-01', '2021-12-31');

Insert into mydb.initial_price values ('Double', 1, 'M', 'regular', 80.0, '2020-01-01', '2021-12-31');
Insert into mydb.initial_price values ('Double', 1, 'T', 'regular', 80.0, '2020-01-01', '2021-12-31');
Insert into mydb.initial_price values ('Double', 1, 'W', 'regular', 80.0,'2020-01-01', '2021-12-31');
Insert into mydb.initial_price values ('Double', 1, 'R', 'regular', 80.0,'2020-01-01', '2021-12-31');
Insert into mydb.initial_price values ('Double', 1, 'F', 'regular', 85.0,'2020-01-01', '2021-12-31');
Insert into mydb.initial_price values ('Double', 1, 'S', 'regular', 90.0,'2020-01-01', '2021-12-31');
Insert into mydb.initial_price values ('Double', 1, 'H', 'regular', 90.0,'2020-01-01', '2021-12-31');

Insert into mydb.initial_price values ('Single', 1, 'M', 'winter', 90.0, '2020-12-01', '2021-02-28');
Insert into mydb.initial_price values ('Single', 1, 'T', 'winter', 90.0, '2020-12-01', '2021-02-28');
Insert into mydb.initial_price values ('Single', 1, 'W', 'winter', 90.0, '2020-12-01', '2021-02-28');
Insert into mydb.initial_price values ('Single', 1, 'R', 'winter', 90.0, '2020-12-01', '2021-02-28' );
Insert into mydb.initial_price values ('Single', 1, 'F', 'winter', 95.0, '2020-12-01', '2021-02-28');
Insert into mydb.initial_price values ('Single', 1, 'S', 'winter', 105.0, '2020-12-01', '2021-02-28');
Insert into mydb.initial_price values ('Single', 1, 'H', 'winter', 105.0, '2020-12-01', '2021-02-28');

Insert into mydb.initial_price values ('Double', 1, 'M', 'winter', 120.0, '2020-12-01', '2021-02-28');
Insert into mydb.initial_price values ('Double', 1, 'T', 'winter', 120.0, '2020-12-01', '2021-02-28');
Insert into mydb.initial_price values ('Double', 1, 'W', 'winter', 120.0, '2020-12-01', '2021-02-28');
Insert into mydb.initial_price values ('Double', 1, 'R', 'winter', 120.0, '2020-12-01', '2021-02-28');
Insert into mydb.initial_price values ('Double', 1, 'F', 'winter', 125.0, '2020-12-01', '2021-02-28');
Insert into mydb.initial_price values ('Double', 1, 'S', 'winter', 135.0, '2020-12-01', '2021-02-28');
Insert into mydb.initial_price values ('Double', 1, 'H', 'winter', 135.0, '2020-12-01', '2021-02-28');

-- additional features (services)
insert into mydb.additional_feature values('Film', 10.0, '00:00:00.0', '23:59:59.9', 0);
insert into mydb.additional_feature values('Film', 10.0, '00:00:00.0', '23:59:59.9', 1);

Insert into mydb.guest values(0, 'Mona Rizvi', 'ID', '7777777', 'USA', '893212', '+15417543010', 'Mona', 'pass');
Insert into mydb.guest values(1, 'Jon Smith', 'ID', '1111111', 'USA', '411241', '+12131242151', 'Jon', 'pass');

Insert into mydb.guest_belongs_category values('Gold', 0);
Insert into mydb.guest_belongs_category values ('None', 1);

-- reservation of Jon Smith for question 2
Insert into mydb.reserves values('Double', 1, 1, '2020-12-06', '2020-12-07', 1);

-- inserting info about stay of a person in room 311 on October 15 for question 3
Insert into mydb.reserves values('Single', 0, 1, '2020-10-15', '2020-10-15', 1);
Insert into mydb.single_stay values('2020-10-15', '2020-10-15', 60.0, '311', 1, 1, 'Single', 0);
Insert into mydb.occupies values('311', 1, 1, '2020-10-15', '2020-10-15', 0, 'Single');