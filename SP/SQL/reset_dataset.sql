select table_name from user_tables;
select * from user_sequences where sequence_name='ORDER_SEQ';

drop sequence order_seq;
create sequence order_seq minvalue 12000001 start with 12000001 increment by 1;

delete from h_lineitem where L_ORDERKEY>=12000001;
delete from h_order where O_ORDERKEY>=12000001;

exec DBMS_MVIEW.PURGE_LOG( master => 'H_ORDER');
exec DBMS_MVIEW.PURGE_LOG( master => 'H_LINEITEM');
exec DBMS_MVIEW.PURGE_LOG( master => 'H_CUSTOMER');
exec DBMS_MVIEW.PURGE_LOG( master => 'H_NATION');
exec DBMS_MVIEW.PURGE_LOG( master => 'H_REGION');
exec DBMS_MVIEW.PURGE_LOG( master => 'H_SUPPLIER');

exec DBMS_MVIEW.REFRESH('FORECASTING_REVENUE_CHANGE', 'C');
exec DBMS_MVIEW.REFRESH('LOCAL_SUPPLIER_VOLUME', 'C');
exec DBMS_MVIEW.REFRESH('PRICING_SUMMARY_REPORT', 'C');
exec DBMS_MVIEW.REFRESH('RETURNED_ITEM_REPORTING', 'C');
exec DBMS_MVIEW.REFRESH('SHIPPING_MOD_AND_ORDER_PRIO', 'C');
exec DBMS_MVIEW.REFRESH('SHIPPING_PRIORITY', 'C');
