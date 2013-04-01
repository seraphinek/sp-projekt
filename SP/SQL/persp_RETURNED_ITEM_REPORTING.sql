-- readable format
CREATE MATERIALIZED VIEW RETURNED_ITEM_REPORTING 
  BUILD IMMEDIATE 
  REFRESH FAST 
as 
select 
  c_custkey, 
  c_name, 
  sum(l_extendedprice * (1 - l_discount)) as revenue, 
  c_acctbal, 
  n_name, 
  c_address, 
  c_phone, 
  c_comment, 
  count(l_extendedprice * (1 - l_discount)) 
from 
  h_customer, 
  h_order, 
  h_lineitem, 
  h_nation 
where 
  c_custkey = o_custkey 
and 
  l_orderkey = o_orderkey 
and 
  o_orderdate >= to_date('1993-10-01','YYYY-MM-DD') 
and 
  o_orderdate < to_date('1993-10-01','YYYY-MM-DD') + interval '3' month 
and 
  l_returnflag = 'R' 
and 
  c_nationkey = n_nationkey 
group by 
  c_custkey, 
  c_name, 
  c_acctbal, 
  c_phone, 
  n_name, 
  c_address, 
  c_comment 
order by 
  revenue desc;


-- sqlplus create mv single line command --
CREATE MATERIALIZED VIEW RETURNED_ITEM_REPORTING BUILD IMMEDIATE REFRESH FAST as select c_custkey, c_name, sum(l_extendedprice * (1 - l_discount)) as revenue, c_acctbal, n_name, c_address, c_phone, c_comment, count(l_extendedprice * (1 - l_discount)) from h_customer, h_order, h_lineitem, h_nation where c_custkey = o_custkey and l_orderkey = o_orderkey and o_orderdate >= to_date('1993-10-01','YYYY-MM-DD') and o_orderdate < to_date('1993-10-01','YYYY-MM-DD') + interval '3' month and l_returnflag = 'R' and c_nationkey = n_nationkey group by c_custkey, c_name, c_acctbal, c_phone, n_name, c_address, c_comment order by revenue desc;


-- explain why couldn't be as fast refresh--
exec dbms_mview.explain_mview('select c_custkey, c_name, sum(l_extendedprice * (1 - l_discount)) as revenue, c_acctbal, n_name, c_address, c_phone, c_comment, count(l_extendedprice * (1 - l_discount)) from h_customer, h_order, h_lineitem, h_nation where c_custkey = o_custkey and l_orderkey = o_orderkey and o_orderdate >= to_date(''1993-10-01'',''YYYY-MM-DD'') and o_orderdate < to_date(''1993-10-01'',''YYYY-MM-DD'') + interval ''3'' month and l_returnflag = ''R'' and c_nationkey = n_nationkey group by c_custkey, c_name, c_acctbal, c_phone, n_name, c_address, c_comment order by revenue desc','5');

select capability_name, possible, related_text, msgtxt from mv_capabilities_table where statement_id='5';
