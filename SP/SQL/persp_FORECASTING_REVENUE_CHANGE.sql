-- readable format
CREATE MATERIALIZED VIEW FORECASTING_REVENUE_CHANGE 
  BUILD IMMEDIATE 
  REFRESH FAST 
as 
select 
  sum(l_extendedprice*l_discount) as revenue, 
  count (l_extendedprice*l_discount) 
from 
  h_lineitem 
where 
  l_shipdate >= to_date('1994-01-01','YYYY-MM-DD') 
and 
  l_shipdate < to_date('1994-01-01','YYYY-MM-DD') + interval '1' year 
and 
  l_discount 
between 
  0.06 - 0.01 and 0.06 + 0.01 
and 
  l_quantity < 24;


-- sqlplus create mv single line command --
CREATE MATERIALIZED VIEW FORECASTING_REVENUE_CHANGE BUILD IMMEDIATE REFRESH FAST as select sum(l_extendedprice*l_discount) as revenue, count (l_extendedprice*l_discount) from h_lineitem where l_shipdate >= to_date('1994-01-01','YYYY-MM-DD') and l_shipdate < to_date('1994-01-01','YYYY-MM-DD') + interval '1' year and l_discount between 0.06 - 0.01 and 0.06 + 0.01 and l_quantity < 24;


-- explain why couldn't be as fast refresh--
exec dbms_mview.explain_mview('select sum(l_extendedprice*l_discount) as revenue, count (l_extendedprice*l_discount) from h_lineitem where l_shipdate >= to_date(''1994-01-01'',''YYYY-MM-DD'') and l_shipdate < to_date(''1994-01-01'',''YYYY-MM-DD'') + interval ''1'' year and l_discount between 0.06 - 0.01 and 0.06 + 0.01 and l_quantity < 24','4');

select capability_name, possible, related_text, msgtxt from mv_capabilities_table where statement_id='4';
