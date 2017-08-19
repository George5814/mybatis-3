/**
 *    Copyright 2009-2017 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.session;

import java.sql.Connection;

/**
 *
 * 事务隔离级别
 * @author Clinton Begin
 */
public enum TransactionIsolationLevel {
  
  /*
  脏读：事务B会读取事务A更改但是没有提交的数据，而事务A回滚事务后，导致事务B读取到脏数据。（一个事务可以读取另一个事务的未提交更改）
  不可重复读：事务A和事务B同时对一条记录发起事务，起始读取时A和B读取一样，但是在A提交了事务后，导致B再次读取对数据与第一次不一致。
        （账户a为2000元，A和B都在消费读取到数据为2000元，A迅速消费了2000元将事务提交，而B在消费时发现没钱了，这就是不可重复读）
        可以对一条记录同时发起两个及以上的事务。
   幻读：事务A读取满足where条件所有的行，而事务B插入了符合where条件的一行。而事务A以同样条件再次读取时，出现了幻像
   */
  
  /** 不支持事务 */
  NONE(Connection.TRANSACTION_NONE),
  /** （可读其他事务未提交的数据）脏读，不可重复读和幻读都会发生，*/
  READ_UNCOMMITTED(Connection.TRANSACTION_READ_UNCOMMITTED),
  /** 脏读可以避免，但不可重复读和幻读还是会发生，该级别仅禁止事务读取未提交的更改（读已经提交的数据） */
  READ_COMMITTED(Connection.TRANSACTION_READ_COMMITTED),
  /** 脏读和不可重复读被避免，但是还是会发生幻读。该事务禁止读取自己未提交的改变（即脏读）。
   * 而且也禁止在第一个事务读取的，第二个事务在修改该行，导致的第一个事务再次读取数据时不一致情况（即不可重复读）*/
  REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ),
  /** 脏读，幻读和不可重复读都避免 */
  SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE);

  private final int level;

  TransactionIsolationLevel(int level) {
    this.level = level;
  }

  public int getLevel() {
    return level;
  }
}
