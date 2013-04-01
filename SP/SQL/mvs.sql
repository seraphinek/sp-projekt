-- mv usefull commands --

-- drop mv on particular table --
drop materialized view on h_lineitem;
drop materialized view on h_customer;
drop materialized view on h_order;
drop materialized view on h_supplier;
drop materialized view on h_nation;
drop materialized view on h_region;

-- drop particular mv --
drop materialized view h_lineitem;
drop materialized view h_customer;
drop materialized view h_order;
drop materialized view h_supplier;
drop materialized view h_nation;
drop materialized view h_region;

-- display mv log content --
select * from PRICING_SUMMARY_REPORT;
select * from LOCAL_SUPPLIER_VOLUME;
select * from FORECASTING_REVENUE_CHANGE;
select * from RETURNED_ITEM_REPORTING;
select * from SHIPPING_MOD_AND_ORDER_PRIO;
select * from SHIPPING_PRIORITY;

-- mvs logs info table -- 
select * from user_mviews where OWNER='TPCH';
