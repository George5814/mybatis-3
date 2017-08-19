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
package org.apache.ibatis.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.executor.result.ResultMapException;
import org.apache.ibatis.session.Configuration;

/**
 *
 * 所有类型处理的基本抽象类，该类对参数设置和返回结果进行了处理。
 * 使用了模板方法模式，使得继承该抽象类的子类实现模板方法，以完成各自的功能。
 * @author Clinton Begin
 * @author Simone Tripodi
 */
public abstract class BaseTypeHandler<T> extends TypeReference<T> implements TypeHandler<T> {

  protected Configuration configuration;

  public void setConfiguration(Configuration c) {
    this.configuration = c;
  }

  public void setParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
    if (parameter == null) {
      if (jdbcType == null) {
        throw new TypeException("JDBC requires that the JdbcType must be specified for all nullable parameters.");
      }
      try {
        ps.setNull(i, jdbcType.TYPE_CODE);
      } catch (SQLException e) {
        throw new TypeException("Error setting null for parameter #" + i + " with JdbcType " + jdbcType + " . " +
                "Try setting a different JdbcType for this parameter or a different jdbcTypeForNull configuration property. " +
                "Cause: " + e, e);
      }
    } else {
      try {
        //应用模板方法
        setNonNullParameter(ps, i, parameter, jdbcType);
      } catch (Exception e) {
        throw new TypeException("Error setting non null for parameter #" + i + " with JdbcType " + jdbcType + " . " +
                "Try setting a different JdbcType for this parameter or a different configuration property. " +
                "Cause: " + e, e);
      }
    }
  }

  public T getResult(ResultSet rs, String columnName) throws SQLException {
    T result;
    try {
      //应用模板方法
      result = getNullableResult(rs, columnName);
    } catch (Exception e) {
      throw new ResultMapException("Error attempting to get column '" + columnName + "' from result set.  Cause: " + e, e);
    }
    if (rs.wasNull()) {
      return null;
    } else {
      return result;
    }
  }

  public T getResult(ResultSet rs, int columnIndex) throws SQLException {
    T result;
    try {
      //模板方法
      result = getNullableResult(rs, columnIndex);
    } catch (Exception e) {
      throw new ResultMapException("Error attempting to get column #" + columnIndex+ " from result set.  Cause: " + e, e);
    }
    if (rs.wasNull()) {
      return null;
    } else {
      return result;
    }
  }
  
  /**
   * 存储过程的返回结果
   * @param cs
   * @param columnIndex
   * @return
   * @throws SQLException
   */
  public T getResult(CallableStatement cs, int columnIndex) throws SQLException {
    T result;
    try {
      //模板方法，在子类中实现
      result = getNullableResult(cs, columnIndex);
    } catch (Exception e) {
      throw new ResultMapException("Error attempting to get column #" + columnIndex+ " from callable statement.  Cause: " + e, e);
    }
    if (cs.wasNull()) {
      return null;
    } else {
      return result;
    }
  }
  

  public abstract void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException;

  public abstract T getNullableResult(ResultSet rs, String columnName) throws SQLException;

  public abstract T getNullableResult(ResultSet rs, int columnIndex) throws SQLException;

  public abstract T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException;

}
