-- readable format
CREATE MATERIALIZED VIEW SHIPPING_PRIORITY 
  BUILD IMMEDIATE 
  REFRESH FAST 
as 
select 
  l_orderkey, 
  sum(l_extendedprice*(1-l_discount)) as revenue, 
  o_orderdate, 
  o_shippriority, 
  count(l_extendedprice*(1-l_discount)) 
from 
  h_customer, 
  h_order, 
  h_lineitem 
where 
  c_mktsegment = 'BUILDING' 
and 
  c_custkey = o_custkey 
and 
  l_orderkey = o_orderkey 
and 
  o_orderdate < to_date('1995-03-15','YYYY-MM-DD') 
and 
  l_shipdate > to_date('1995-03-15','YYYY-MM-DD') 
group by 
  l_orderkey, 
  o_orderdate, 
  o_shippriority 
order by 
  revenue desc, 
  o_orderdate;


-- sqlplus create mv single line command --
CREATE MATERIALIZED VIEW SHIPPING_PRIORITY BUILD IMMEDIATE REFRESH FAST as select l_orderkey, sum(l_extendedprice*(1-l_discount)) as revenue, o_orderdate, o_shippriority, count(l_extendedprice*(1-l_discount)) from h_customer, h_order, h_lineitem where c_mktsegment = 'BUILDING' and c_custkey = o_custkey and l_orderkey = o_orderkey and o_orderdate < to_date('1995-03-15','YYYY-MM-DD') and l_shipdate > to_date('1995-03-15','YYYY-MM-DD') group by l_orderkey, o_orderdate, o_shippriority order by revenue desc, o_orderdate;

-- explain why couldn't be as fast refresh--
exec dbms_mview.explain_mview('select l_orderkey, sum(l_extendedprice*(1-l_discount)) as revenue, o_orderdate, o_shippriority, count(l_extendedprice*(1-l_discount)) from h_customer, h_orders, h_lineitem where c_mktsegment = ''BUILDING'' and c_custkey = o_custkey and l_orderkey = o_orderkey and o_orderdate < to_date(''1995-03-15'',''YYYY-MM-DD'') and l_shipdate > to_date(''1995-03-15'',''YYYY-MM-DD'') group by l_orderkey, o_orderdate, o_shippriority order by revenue desc, o_orderdate','2');

select capability_name, possible, related_text, msgtxt from mv_capabilities_table where statement_id='2';


