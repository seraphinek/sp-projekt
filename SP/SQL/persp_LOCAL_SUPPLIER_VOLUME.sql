-- readable format
CREATE MATERIALIZED VIEW LOCAL_SUPPLIER_VOLUME 
  BUILD IMMEDIATE 
  REFRESH FAST 
as 
select 
  n_name, 
  sum(l_extendedprice * (1 - l_discount)) as revenue, 
  count(l_extendedprice * (1 - l_discount)) 
from 
  h_customer, 
  h_order, 
  h_lineitem, 
  h_supplier, 
  h_nation, 
  h_region 
where 
  c_custkey = o_custkey 
and 
  l_orderkey = o_orderkey 
and 
  l_suppkey = s_suppkey 
and 
  c_nationkey = s_nationkey 
and 
  s_nationkey = n_nationkey 
and 
  n_regionkey = r_regionkey 
and 
  r_name = 'ASIA' 
and 
  o_orderdate >= to_date('1994-01-01','YYYY-MM-DD') 
and 
  o_orderdate < to_date('1994-01-01','YYYY-MM-DD') + interval '1' year 
group by 
  n_name 
order by 
  revenue desc;


-- sqlplus create mv single line command --
CREATE MATERIALIZED VIEW LOCAL_SUPPLIER_VOLUME BUILD IMMEDIATE REFRESH FAST as select n_name, sum(l_extendedprice * (1 - l_discount)) as revenue, count(l_extendedprice * (1 - l_discount)) from h_customer, h_order, h_lineitem, h_supplier, h_nation, h_region where c_custkey = o_custkey and l_orderkey = o_orderkey and l_suppkey = s_suppkey and c_nationkey = s_nationkey and s_nationkey = n_nationkey and n_regionkey = r_regionkey and r_name = 'ASIA' and o_orderdate >= to_date('1994-01-01','YYYY-MM-DD') and o_orderdate < to_date('1994-01-01','YYYY-MM-DD') + interval '1' year group by n_name order by revenue desc;


-- explain why couldn't be as fast refresh--
exec dbms_mview.explain_mview('select n_name, sum(l_extendedprice * (1 - l_discount)) as revenue, count(l_extendedprice * (1 - l_discount)) from h_customer, h_order, h_lineitem, h_supplier, h_nation, h_region where c_custkey = o_custkey and l_orderkey = o_orderkey and l_suppkey = s_suppkey and c_nationkey = s_nationkey and s_nationkey = n_nationkey and n_regionkey = r_regionkey and r_name = ''ASIA'' and o_orderdate >= to_date(''1994-01-01'',''YYYY-MM-DD'') and o_orderdate < to_date(''1994-01-01'',''YYYY-MM-DD'') + interval ''1'' year group by n_name order by revenue desc','3');

select capability_name, possible, related_text, msgtxt from mv_capabilities_table where statement_id='3';
