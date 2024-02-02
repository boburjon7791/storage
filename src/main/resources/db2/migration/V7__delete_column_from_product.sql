alter table product
    add constraint not_negative_price CHECK (price>=0);
alter table product
drop column date_time;
