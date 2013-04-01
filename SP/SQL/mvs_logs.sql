-- each one mv log below is needed for current mvs set --

-- create mv log for particular table
CREATE MATERIALIZED VIEW LOG ON h_lineitem WITH PRIMARY KEY, ROWID, SEQUENCE (l_returnflag, l_linestatus, l_shipdate, l_quantity, l_extendedprice, l_discount, l_tax, l_commitdate, l_receiptdate, l_suppkey, l_shipmode) INCLUDING NEW VALUES;

CREATE MATERIALIZED VIEW LOG ON h_customer WITH PRIMARY KEY, ROWID, SEQUENCE (c_mktsegment, c_nationkey, c_name, c_acctbal, c_address, c_phone, c_comment) INCLUDING NEW VALUES;

CREATE MATERIALIZED VIEW LOG ON h_order WITH PRIMARY KEY, ROWID, SEQUENCE (o_orderdate, o_shippriority, o_custkey, o_orderpriority) INCLUDING NEW VALUES;

CREATE MATERIALIZED VIEW LOG ON h_supplier WITH PRIMARY KEY, ROWID, SEQUENCE(s_nationkey) INCLUDING NEW VALUES;

CREATE MATERIALIZED VIEW LOG ON h_nation WITH PRIMARY KEY, ROWID, SEQUENCE(n_regionkey, n_name) INCLUDING NEW VALUES;

CREATE MATERIALIZED VIEW LOG ON h_region WITH PRIMARY KEY, ROWID, SEQUENCE(r_name) INCLUDING NEW VALUES;


-- drop mv log --
drop materialized view log on h_lineitem;
drop materialized view log on h_customer;
drop materialized view log on h_order;
drop materialized view log on h_supplier;
drop materialized view log on h_nation;
drop materialized view log on h_region;


-- display mv log content --
select * from MLOG$_H_CUSTOMER;
select * from MLOG$_H_LINEITEM;
select * from MLOG$_H_NATION;
select * from MLOG$_H_ORDER;
select * from MLOG$_H_REGION;
select * from MLOG$_H_SUPPLIER;

-- mvs logs info table -- 
select * from user_mview_logs where LOG_OWNER='TPCH';
