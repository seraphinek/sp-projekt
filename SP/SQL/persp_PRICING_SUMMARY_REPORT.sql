-- readable format
CREATE MATERIALIZED VIEW PRICING_SUMMARY_REPORT 
  BUILD IMMEDIATE 
  REFRESH FAST 
as 
select  
  l_returnflag,  
  l_linestatus,  
  sum(l_quantity) as sum_qty,  
  sum(l_extendedprice) as sum_base_price,  
  sum(l_extendedprice*(1-l_discount)) as sum_disc_price,  
  sum(l_extendedprice*(1-l_discount)*(1+l_tax)) as sum_charge,  
  avg(l_quantity) as avg_qty,  
  avg(l_extendedprice) as avg_price,  
  avg(l_discount) as avg_disc,  
  count(*) as count_order, 
  count(l_extendedprice*(1-l_discount)), 
  count(l_extendedprice*(1-l_discount)*(1+l_tax)), 
  count(l_quantity), 
  count(l_extendedprice), 
  count(l_discount) 
from 
  h_lineitem 
where  
  l_shipdate <= to_date('01-12-1998','MM-DD-YYYY') - interval '90' day (3) 
group by 
  l_returnflag, 
  l_linestatus 
order by 
  l_returnflag, 
  l_linestatus;


-- sqlplus create mv single line command --
CREATE MATERIALIZED VIEW PRICING_SUMMARY_REPORT BUILD IMMEDIATE REFRESH FAST as select  l_returnflag,  l_linestatus,  sum(l_quantity) as sum_qty,  sum(l_extendedprice) as sum_base_price,  sum(l_extendedprice*(1-l_discount)) as sum_disc_price,  sum(l_extendedprice*(1-l_discount)*(1+l_tax)) as sum_charge,  avg(l_quantity) as avg_qty,  avg(l_extendedprice) as avg_price,  avg(l_discount) as avg_disc,  count(*) as count_order, count(l_extendedprice*(1-l_discount)), count(l_extendedprice*(1-l_discount)*(1+l_tax)), count(l_quantity), count(l_extendedprice), count(l_discount) from h_lineitem where  l_shipdate <= to_date('01-12-1998','MM-DD-YYYY') - interval '90' day (3) group by l_returnflag, l_linestatus order by l_returnflag, l_linestatus;


-- explain why couldn't be as fast refresh--
exec dbms_mview.explain_mview('select  l_returnflag,  l_linestatus,  sum(l_quantity) as sum_qty,  sum(l_extendedprice) as sum_base_price,  sum(l_extendedprice*(1-l_discount)) as sum_disc_price,  sum(l_extendedprice*(1-l_discount)*(1+l_tax)) as sum_charge,  avg(l_quantity) as avg_qty,  avg(l_extendedprice) as avg_price,  avg(l_discount) as avg_disc,  count(*) as count_order, count(l_extendedprice*(1-l_discount)), count(l_extendedprice*(1-l_discount)*(1+l_tax)), count(l_quantity), count(l_extendedprice), count(l_discount) from h_lineitem where  l_shipdate <= to_date(''01-12-1998'',''MM-DD-YYYY'') - interval ''90'' day (3) group by l_returnflag, l_linestatus order by l_returnflag, l_linestatus', '1');

select capability_name, possible, related_text, msgtxt from mv_capabilities_table where statement_id='1';


