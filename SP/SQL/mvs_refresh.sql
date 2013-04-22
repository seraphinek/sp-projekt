create table T_FORECASTING_REVENUE_CHANGE as select sum(l_extendedprice*l_discount) as revenue from h_lineitem where l_shipdate >= to_date('1994-01-01','YYYY-MM-DD') and l_shipdate < to_date('1994-01-01','YYYY-MM-DD') + interval '1' year and l_discount between 0.06 - 0.01 and 0.06 + 0.01 and l_quantity < 24;

create table T_LOCAL_SUPPLIER_VOLUME as select n_name, sum(l_extendedprice * (1 - l_discount)) as revenue from h_customer, h_order, h_lineitem, h_supplier, h_nation, h_region where c_custkey = o_custkey and l_orderkey = o_orderkey and l_suppkey = s_suppkey and c_nationkey = s_nationkey and s_nationkey = n_nationkey and n_regionkey = r_regionkey and r_name = 'ASIA' and o_orderdate >= to_date('1994-01-01','YYYY-MM-DD') and o_orderdate < to_date('1994-01-01','YYYY-MM-DD') + interval '1' year group by n_name order by revenue desc;

create table T_PRICING_SUMMARY_REPORT  as select  l_returnflag,  l_linestatus,  sum(l_quantity) as sum_qty,  sum(l_extendedprice) as sum_base_price,  sum(l_extendedprice*(1-l_discount)) as sum_disc_price,  sum(l_extendedprice*(1-l_discount)*(1+l_tax)) as sum_charge,  avg(l_quantity) as avg_qty,  avg(l_extendedprice) as avg_price,  avg(l_discount) as avg_disc,  count(*) as count_order, count(l_quantity) as avg_qty_c, count(l_extendedprice) as avg_price_c, count(l_discount) as avg_disc_c, sum(l_discount) as sum_disc from h_lineitem where  l_shipdate <= to_date('01-12-1998','MM-DD-YYYY') - interval '90' day (3) group by l_returnflag, l_linestatus order by l_returnflag, l_linestatus;

create table T_RETURNED_ITEM_REPORTING as select c_custkey, c_name, sum(l_extendedprice * (1 - l_discount)) as revenue, c_acctbal, n_name, c_address, c_phone, c_comment from h_customer, h_order, h_lineitem, h_nation where c_custkey = o_custkey and l_orderkey = o_orderkey and o_orderdate >= to_date('1993-10-01','YYYY-MM-DD') and o_orderdate < to_date('1993-10-01','YYYY-MM-DD') + interval '3' month and l_returnflag = 'R' and c_nationkey = n_nationkey group by c_custkey, c_name, c_acctbal, c_phone, n_name, c_address, c_comment order by revenue desc;

create table T_SHIPPING_MOD_AND_ORDER_PRIO as select l_shipmode, sum(case when o_orderpriority ='1-URGENT' or o_orderpriority ='2-HIGH' then 1 else 0 end) as high_line_count, sum(case when o_orderpriority <> '1-URGENT' and o_orderpriority <> '2-HIGH' then 1 else 0 end) as low_line_count from h_order, h_lineitem where o_orderkey = l_orderkey and l_shipmode in ('MAIL', 'SHIP') and l_commitdate < l_receiptdate and l_shipdate < l_commitdate and l_receiptdate >= to_date('1994-01-01','YYYY-MM-DD') and l_receiptdate < to_date('1994-01-01','YYYY-MM-DD') + interval '1' year group by l_shipmode order by l_shipmode;

create table T_SHIPPING_PRIORITY as select l_orderkey, sum(l_extendedprice*(1-l_discount)) as revenue, o_orderdate, o_shippriority from h_customer, h_order, h_lineitem where c_mktsegment = 'BUILDING' and c_custkey = o_custkey and l_orderkey = o_orderkey and o_orderdate < to_date('1995-03-15','YYYY-MM-DD') and l_shipdate > to_date('1995-03-15','YYYY-MM-DD') group by l_orderkey, o_orderdate, o_shippriority order by revenue desc, o_orderdate;

create table global_log(TYPE CHAR(1), O_ORDERKEY NUMBER(10), O_CUSTKEY NUMBER(10), O_ORDERSTATUS CHAR(1), O_TOTALPRICE NUMBER, O_ORDERDATE DATE, O_ORDERPRIORITY VARCHAR2(15), O_CLERK VARCHAR2(15), O_SHIPPRIORITY NUMBER(38), O_COMMENT VARCHAR2(79), L_ORDERKEY NUMBER(10), L_PARTKEY NUMBER(10), L_SUPPKEY NUMBER(10), L_LINENUMBER NUMBER(38), L_QUANTITY NUMBER, L_EXTENDEDPRICE NUMBER, L_DISCOUNT NUMBER, L_TAX NUMBER, L_RETURNFLAG CHAR(1), L_LINESTATUS CHAR(1), L_SHIPDATE DATE, L_COMMITDATE DATE, L_RECEIPTDATE DATE, L_SHIPINSTRUCT VARCHAR2(25), L_SHIPMODE VARCHAR2(10), L_COMMENT VARCHAR2(44));

create table refresh_semaphore
( is_refreshing NUMBER );

--===============================================================================================================================

--------------------------------------------------------------------------------------------------------

create or replace trigger copy_order_to_global_log 
 after insert on h_order 
 for each row 
begin 
 insert into global_log(TYPE, O_ORDERKEY, O_CUSTKEY, O_ORDERSTATUS, O_TOTALPRICE, O_ORDERDATE, O_ORDERPRIORITY, O_CLERK, O_SHIPPRIORITY, O_COMMENT) 
 VALUES('O', :new.O_ORDERKEY, :new.O_CUSTKEY, :new.O_ORDERSTATUS, :new.O_TOTALPRICE, :new.O_ORDERDATE, :new.O_ORDERPRIORITY, :new.O_CLERK, :new.O_SHIPPRIORITY, :new.O_COMMENT); 
end;

------------------------------------------------------------------------------

create or replace trigger copy_lineitem_to_global_log 
 after insert on h_lineitem 
 for each row 
begin 
 insert into global_log(TYPE, L_ORDERKEY, L_PARTKEY, L_SUPPKEY, L_LINENUMBER, L_QUANTITY, L_EXTENDEDPRICE, L_DISCOUNT, L_TAX, L_RETURNFLAG, L_LINESTATUS, L_SHIPDATE,  L_COMMITDATE, L_RECEIPTDATE, L_SHIPINSTRUCT, L_SHIPMODE, L_COMMENT) 
 VALUES('L', :new.L_ORDERKEY, :new.L_PARTKEY, :new.L_SUPPKEY, :new.L_LINENUMBER, :new.L_QUANTITY, :new.L_EXTENDEDPRICE, :new.L_DISCOUNT, :new.L_TAX, :new.L_RETURNFLAG, :new.L_LINESTATUS, :new.L_SHIPDATE,  :new.L_COMMITDATE, :new.L_RECEIPTDATE, :new.L_SHIPINSTRUCT, :new.L_SHIPMODE, :new.L_COMMENT); end;

------------------------------------------------------------------------------

create or replace trigger copy_rows_to_refresh 
 after insert on global_log 
declare 
 v_lineitem_array lineitem_array := lineitem_array(); 
 v_order_array order_array := order_array(); 

 v_refreshing NUMBER;
 v_transaction_limit NUMBER; 
 v_current_counter NUMBER;  
 v_new_rows_count NUMBER; 
 v_instr_per_trans NUMBER;
 v_temp_counter_li NUMBER;
 v_temp_counter_ord NUMBER;
begin 
 select is_refreshing 
 into v_refreshing
 from refresh_semaphore
 where rownum = 1;

 if v_refreshing = 0 then
  update refresh_semaphore 
  set is_refreshing = 1 
  where rownum = 1;

  select limit 
  into v_transaction_limit 
  from transactionsLimit 
  where rownum=1; 

  select limit 
  into v_instr_per_trans 
  from insertsPerTransactionLimit 
  where rownum=1; 

  select count(*) 
  into v_new_rows_count 
  from global_log 
  where type='O'; 

  if v_new_rows_count >= (v_instr_per_trans * v_transaction_limit) then 
   v_current_counter := 0;
   v_temp_counter_li := 1;
   v_temp_counter_ord := 1;

   for rec in (select * from global_log) loop
    
    if v_current_counter <= v_instr_per_trans * v_transaction_limit then
     if rec.type = 'L' then
      v_lineitem_array.extend;
      v_lineitem_array(v_temp_counter_li) := new lineitem_rec(rec.L_ORDERKEY, rec.L_PARTKEY,
        rec.L_SUPPKEY, rec.L_LINENUMBER, rec.L_QUANTITY, rec.L_EXTENDEDPRICE,
        rec.L_DISCOUNT, rec.L_TAX, rec.L_RETURNFLAG, rec.L_LINESTATUS, rec.L_SHIPDATE,  
        rec.L_COMMITDATE, rec.L_RECEIPTDATE, rec.L_SHIPINSTRUCT, rec.L_SHIPMODE, rec.L_COMMENT);
      
      v_temp_counter_li := v_temp_counter_li + 1;
     else
      v_order_array.extend;
      v_order_array(v_temp_counter_ord) := new order_rec(rec.O_ORDERKEY, 
        rec.O_CUSTKEY, rec.O_ORDERSTATUS, rec.O_TOTALPRICE, rec.O_ORDERDATE, 
        rec.O_ORDERPRIORITY, rec.O_CLERK, rec.O_SHIPPRIORITY, rec.O_COMMENT);

      v_temp_counter_ord := v_temp_counter_ord + 1;
      v_current_counter := v_current_counter + 1;
     end if;
    else
     exit;
    end if;
   end loop;

   delete from global_log 
   where rownum < (v_temp_counter_ord + v_temp_counter_li);

   F_FORECASTING_REVENUE_CHANGE(v_lineitem_array);
   F_LOCAL_SUPPLIER_VOLUME(v_lineitem_array, v_order_array);
   F_PRICING_SUMMARY_REPORT(v_lineitem_array);
   F_RETURNED_ITEM_REPORTING(v_lineitem_array, v_order_array);
   F_SHIPPING_MOD_AND_ORDER_PRIO(v_lineitem_array, v_order_array);
   F_SHIPPING_PRIORITY(v_lineitem_array, v_order_array);

  end if;

  update refresh_semaphore 
  set is_refreshing = 0
  where rownum = 1;
 end if;
end; 


------------------------------------------------------------------------------

create or replace procedure F_FORECASTING_REVENUE_CHANGE
 (h_lineitem_temp IN lineitem_array) IS
 v_revenue NUMBER;
begin
 select sum(l_extendedprice*l_discount) into v_revenue 
 from table(h_lineitem_temp) 
 where l_shipdate >= to_date('1994-01-01','YYYY-MM-DD') 
 and l_shipdate < to_date('1994-01-01','YYYY-MM-DD') + interval '1' year 
 and l_discount between 0.06 - 0.01 and 0.06 + 0.01 
 and l_quantity < 24; 

 update T_FORECASTING_REVENUE_CHANGE set revenue = revenue+v_revenue;
end;

--------------------------------------------------------------------------------

create or replace procedure F_LOCAL_SUPPLIER_VOLUME
 (h_lineitem_temp IN lineitem_array,
  h_order_temp IN order_array) IS
 v_exists NUMBER;
begin
 for rec in 
  (
   select n_name, sum(l_extendedprice * (1 - l_discount)) as revenue 
   from h_customer, table(h_order_temp), table(h_lineitem_temp), h_supplier, h_nation, h_region 
   where c_custkey = o_custkey 
   and l_orderkey = o_orderkey 
   and l_suppkey = s_suppkey 
   and c_nationkey = s_nationkey 
   and s_nationkey = n_nationkey 
   and n_regionkey = r_regionkey 
   and r_name = 'ASIA' 
   and o_orderdate >= to_date('1994-01-01','YYYY-MM-DD') 
   and o_orderdate < to_date('1994-01-01','YYYY-MM-DD') + interval '1' year 
   group by n_name
  ) loop
  
  select count(N_NAME) into v_exists 
  from T_LOCAL_SUPPLIER_VOLUME 
  where n_name = rec.n_name;

  IF v_exists >= 1 then
   update T_LOCAL_SUPPLIER_VOLUME 
   set revenue = revenue + rec.revenue 
   where n_name= rec.n_name;
  else
   insert into T_LOCAL_SUPPLIER_VOLUME(n_name, revenue) 
   values(rec.n_name, rec.revenue);
  end if;

 end loop;
end;

--------------------------------------------------------------------------------

create or replace procedure F_PRICING_SUMMARY_REPORT
 (h_lineitem_temp IN lineitem_array) IS
 v_exists NUMBER;
begin
 for rec in 
  (
   select  l_returnflag,  l_linestatus,  
    sum(l_quantity) as sum_qty,  
    sum(l_extendedprice) as sum_base_price,  
    sum(l_extendedprice*(1-l_discount)) as sum_disc_price,  
    sum(l_extendedprice*(1-l_discount)*(1+l_tax)) as sum_charge,  
    avg(l_quantity) as avg_qty,  
    avg(l_extendedprice) as avg_price,  
    avg(l_discount) as avg_disc,  
    count(*) as count_order, 
    count(l_quantity) as avg_qty_c, 
    count(l_extendedprice) as avg_price_c, 
    count(l_discount) as avg_disc_c,
    sum(l_discount) as sum_disc
   from table(h_lineitem_temp)
   where  l_shipdate <= to_date('01-12-1998','MM-DD-YYYY') - interval '90' day (3) 
   group by l_returnflag, l_linestatus
  ) loop

   select count(*) into v_exists 
   from T_PRICING_SUMMARY_REPORT 
   where L_RETURNFLAG = rec.L_RETURNFLAG
   and L_LINESTATUS = rec.L_LINESTATUS;

   IF v_exists >= 1 then
    update T_PRICING_SUMMARY_REPORT 
    set sum_qty = sum_qty + rec.sum_qty,
     sum_base_price = sum_base_price + rec.sum_base_price,
     sum_disc_price = sum_disc_price + rec.sum_disc_price,
     sum_charge = sum_charge + rec.sum_charge,
     avg_qty = (sum_qty + rec.sum_qty) / (avg_qty_c + rec.avg_qty_c),
     avg_price = (sum_base_price + rec.sum_base_price) / (avg_price_c + rec.avg_price_c),
     avg_disc = (sum_disc_price + rec.sum_disc_price) / (avg_disc_c + rec.avg_disc_c),
     count_order = count_order + rec.count_order,
     avg_qty_c = avg_qty_c + rec.avg_qty_c,
     avg_price_c = avg_price_c + rec.avg_price_c,
     avg_disc_c = avg_disc_c + rec.avg_disc_c,
     sum_disc = sum_disc + rec.sum_disc
    where l_returnflag = rec.l_returnflag 
    and l_linestatus = rec.l_linestatus;
   else
    insert into T_PRICING_SUMMARY_REPORT
    values (
     rec.l_returnflag, rec.l_linestatus,
     rec.sum_qty, 
     rec.sum_base_price,
     rec.sum_disc_price,
     rec.sum_charge,
     rec.sum_qty / rec.avg_qty_c,
     rec.sum_base_price / rec.avg_price_c,
     rec.sum_disc / rec.avg_disc_c,
     rec.count_order,
     rec.avg_qty_c,
     rec.avg_price_c, 
     rec.avg_disc_c,
     rec.sum_disc);
   end if;
  end loop;
end;

--------------------------------------------------------------------------------

create or replace procedure F_RETURNED_ITEM_REPORTING
 (h_lineitem_temp IN lineitem_array,
  h_order_temp IN order_array) IS
 v_exists NUMBER;
begin
 for rec in 
  (
   select c_custkey, c_name, 
    sum(l_extendedprice * (1 - l_discount)) as revenue, 
    c_acctbal, n_name, c_address, c_phone, c_comment 
   from h_customer, table(h_order_temp), table(h_lineitem_temp), h_nation 
   where c_custkey = o_custkey 
   and l_orderkey = o_orderkey 
   and o_orderdate >= to_date('1993-10-01','YYYY-MM-DD') 
   and o_orderdate < to_date('1993-10-01','YYYY-MM-DD') + interval '3' month 
   and l_returnflag = 'R' 
   and c_nationkey = n_nationkey 
   group by c_custkey, c_name, c_acctbal, c_phone, n_name, c_address, c_comment
  ) loop
 
  select count(*) into v_exists 
  from T_RETURNED_ITEM_REPORTING 
  where c_custkey = rec.c_custkey
  and n_name = rec.n_name;

  IF v_exists >= 1 then
   update T_RETURNED_ITEM_REPORTING 
   set c_name = rec.c_name,
     revenue = revenue + rec.revenue,
     c_acctbal = rec.c_acctbal, 
     n_name = rec.n_name, 
     c_address = rec.c_address, 
     c_phone = rec.c_phone, 
     c_comment = rec.c_comment
   where c_custkey = rec.c_custkey
   and n_name = rec.n_name;
  else
   insert into T_RETURNED_ITEM_REPORTING
   values(rec.c_custkey, 
    rec.c_name, 
    rec.revenue, 
    rec.c_acctbal, 
    rec.n_name, 
    rec.c_address, 
    rec.c_phone, 
    rec.c_comment);
  end if;
 end loop;
end;

--------------------------------------------------------------------------------

create or replace procedure F_SHIPPING_MOD_AND_ORDER_PRIO
 (h_lineitem_temp IN lineitem_array,
  h_order_temp IN order_array) IS
 v_exists NUMBER;
begin
 for rec in 
  (
   select l_shipmode, 
    sum(case 
	  when o_orderpriority ='1-URGENT' or o_orderpriority ='2-HIGH' 
	  then 1 
	  else 0 
	end) as high_line_count, 
    sum(case 
	  when o_orderpriority <> '1-URGENT' and o_orderpriority <> '2-HIGH' 
	  then 1 
	  else 0 
	end) as low_line_count
   from table(h_order_temp), table(h_lineitem_temp) 
   where o_orderkey = l_orderkey 
   and l_shipmode in ('MAIL', 'SHIP') 
   and l_commitdate < l_receiptdate 
   and l_shipdate < l_commitdate 
   and l_receiptdate >= to_date('1994-01-01','YYYY-MM-DD') 
   and l_receiptdate < to_date('1994-01-01','YYYY-MM-DD') + interval '1' year 
   group by l_shipmode
  ) loop

   select count(*) into v_exists 
   from T_SHIPPING_MOD_AND_ORDER_PRIO 
   where l_shipmode = rec.l_shipmode;

   IF v_exists >= 1 then
    update T_SHIPPING_MOD_AND_ORDER_PRIO
    set high_line_count = high_line_count + rec.high_line_count,
     low_line_count = low_line_count + rec.low_line_count
    where l_shipmode = rec.l_shipmode;
   else
    insert into T_SHIPPING_MOD_AND_ORDER_PRIO
    values (rec.l_shipmode,
     rec.high_line_count,
     rec.low_line_count);
   end if;

  end loop;
end;

--------------------------------------------------------------------------------

create or replace procedure F_SHIPPING_PRIORITY
 (h_lineitem_temp IN lineitem_array,
  h_order_temp IN order_array) IS
 v_exists NUMBER;
begin
 for rec in 
  (
   select l_orderkey, 
    sum(l_extendedprice*(1-l_discount)) as revenue, 
    o_orderdate, 
    o_shippriority 
   from h_customer, table(h_order_temp), table(h_lineitem_temp) 
   where c_mktsegment = 'BUILDING' 
   and c_custkey = o_custkey 
   and l_orderkey = o_orderkey 
   and o_orderdate < to_date('1995-03-15','YYYY-MM-DD') 
   and l_shipdate > to_date('1995-03-15','YYYY-MM-DD') 
   group by l_orderkey, o_orderdate, o_shippriority
  ) loop

   select count(*) into v_exists 
   from T_SHIPPING_PRIORITY 
   where l_orderkey = rec.l_orderkey
   and o_orderdate = rec.o_orderdate
   and o_shippriority = rec.o_shippriority;

   IF v_exists >= 1 then
    update T_SHIPPING_PRIORITY
    set revenue = revenue + rec.revenue
    where l_orderkey = rec.l_orderkey
    and o_orderdate = rec.o_orderdate
    and o_shippriority = rec.o_shippriority;
   else
    insert into T_SHIPPING_PRIORITY
    values (rec.l_orderkey,
    rec.revenue,
    rec.o_orderdate,
    rec.o_shippriority);
   end if;
  end loop;
end;

--------------------------------------------------------------------------------

create or replace type lineitem_rec as object (
 L_ORDERKEY NUMBER(10),
 L_PARTKEY NUMBER(10),
 L_SUPPKEY NUMBER(10), 
 L_LINENUMBER NUMBER(38),
 L_QUANTITY NUMBER,
 L_EXTENDEDPRICE NUMBER,
 L_DISCOUNT NUMBER,
 L_TAX NUMBER,
 L_RETURNFLAG CHAR(1),
 L_LINESTATUS CHAR(1),
 L_SHIPDATE DATE,
 L_COMMITDATE DATE,
 L_RECEIPTDATE DATE,
 L_SHIPINSTRUCT VARCHAR2(25),
 L_SHIPMODE VARCHAR2(10),
 L_COMMENT VARCHAR2(44)
)

create or replace type lineitem_array as table of lineitem_rec;

create or replace type order_rec as object (
O_ORDERKEY NUMBER(10),
 O_CUSTKEY NUMBER(10),
 O_ORDERSTATUS CHAR(1),
 O_TOTALPRICE NUMBER,
 O_ORDERDATE DATE,
 O_ORDERPRIORITY VARCHAR2(15),
 O_CLERK VARCHAR2(15),
 O_SHIPPRIORITY NUMBER(38),
 O_COMMENT VARCHAR2(79)
);

create or replace type order_array as table of order_rec;


--------------------------------------------------------------

drop table T_FORECASTING_REVENUE_CHANGE;
drop table T_LOCAL_SUPPLIER_VOLUME;
drop table T_PRICING_SUMMARY_REPORT;
drop table T_RETURNED_ITEM_REPORTING;
drop table T_SHIPPING_MOD_AND_ORDER_PRIO;
drop table T_SHIPPING_PRIORITY;
delete from global_log;

insert into refresh_semaphore(is_refreshing) values (0);
