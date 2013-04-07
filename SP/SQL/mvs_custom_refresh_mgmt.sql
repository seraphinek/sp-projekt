-- Create tables with limits

create table transactionsLimit( limit NUMBER );
create table insertsPerTransactionLimit( limit NUMBER );

-- Create oracle jobs 
CREATE OR REPLACE PROCEDURE DBMS_JOB_RF1 IS INT_JOB_ID NUMBER := NULL; BEGIN DBMS_JOB.SUBMIT ( job => INT_JOB_ID, what => 'DBMS_MVIEW.REFRESH(''FORECASTING_REVENUE_CHANGE'', ''F'');', next_date => SYSDATE, interval =>  NULL); END;
/

CREATE OR REPLACE PROCEDURE DBMS_JOB_RF2 IS INT_JOB_ID NUMBER := NULL; BEGIN DBMS_JOB.SUBMIT ( job => INT_JOB_ID, what => 'DBMS_MVIEW.REFRESH(''LOCAL_SUPPLIER_VOLUME'', ''F'');', next_date => SYSDATE, interval =>  NULL); END;
/

CREATE OR REPLACE PROCEDURE DBMS_JOB_RF3 IS INT_JOB_ID NUMBER := NULL; BEGIN DBMS_JOB.SUBMIT ( job => INT_JOB_ID, what => 'DBMS_MVIEW.REFRESH(''PRICING_SUMMARY_REPORT'', ''F'');', next_date => SYSDATE, interval =>  NULL); END;
/

CREATE OR REPLACE PROCEDURE DBMS_JOB_RF4 IS INT_JOB_ID NUMBER := NULL; BEGIN DBMS_JOB.SUBMIT ( job => INT_JOB_ID, what => 'DBMS_MVIEW.REFRESH(''RETURNED_ITEM_REPORTING'', ''F'');', next_date => SYSDATE, interval =>  NULL); END;
/

CREATE OR REPLACE PROCEDURE DBMS_JOB_RF5 IS INT_JOB_ID NUMBER := NULL; BEGIN DBMS_JOB.SUBMIT ( job => INT_JOB_ID, what => 'DBMS_MVIEW.REFRESH(''SHIPPING_MOD_AND_ORDER_PRIO'', ''F'');', next_date => SYSDATE, interval =>  NULL); END;
/

CREATE OR REPLACE PROCEDURE DBMS_JOB_RF6 IS INT_JOB_ID NUMBER := NULL; BEGIN DBMS_JOB.SUBMIT ( job => INT_JOB_ID, what => 'DBMS_MVIEW.REFRESH(''SHIPPING_PRIORITY'', ''F'');', next_date => SYSDATE, interval =>  NULL); END;
/

-- Create trigger (readable format)
create or replace trigger t_manual_refresh 
  after insert on h_order 
declare 
  v_transaction_limit NUMBER; 
  v_new_orders_count NUMBER; 
  v_instr_per_trans NUMBER; 
begin 
  select limit into v_transaction_limit 
  from transactionsLimit
  where rownum=1; 

  select limit into v_instr_per_trans
  from insertsPerTransactionLimit
  where rownum=1; 

  select count(*) into v_new_orders_count
  from MLOG$_H_ORDER; 

  if v_new_orders_count >= v_instr_per_trans * v_transaction_limit then 
    DBMS_JOB_RF1;
    DBMS_JOB_RF2;
    DBMS_JOB_RF3;
    DBMS_JOB_RF4;
    DBMS_JOB_RF5;
    DBMS_JOB_RF6;
  end if; 
end;


-- Create trigger (single line)
create or replace trigger t_manual_refresh after insert on h_order declare v_transaction_limit NUMBER; v_new_orders_count NUMBER; v_instr_per_trans NUMBER; begin select limit into v_transaction_limit from transactionsLimit where rownum=1; select limit into v_instr_per_trans from insertsPerTransactionLimit where rownum=1; select count(*) into v_new_orders_count from MLOG$_H_ORDER; if v_new_orders_count >= v_instr_per_trans * v_transaction_limit then DBMS_JOB_RF1; DBMS_JOB_RF2; DBMS_JOB_RF3; DBMS_JOB_RF4; DBMS_JOB_RF5; DBMS_JOB_RF6; end if; end;

-- Switch trigger
ALTER TRIGGER t_manual_refresh ENABLE;
ALTER TRIGGER t_manual_refresh DISABLE;
