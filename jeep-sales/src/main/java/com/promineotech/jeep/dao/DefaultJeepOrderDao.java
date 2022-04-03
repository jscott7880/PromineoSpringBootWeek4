package com.promineotech.jeep.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import com.promineotech.jeep.entity.Color;
import com.promineotech.jeep.entity.Customer;
import com.promineotech.jeep.entity.Engine;
import com.promineotech.jeep.entity.Jeep;
import com.promineotech.jeep.entity.JeepModel;
import com.promineotech.jeep.entity.Option;
import com.promineotech.jeep.entity.OptionType;
import com.promineotech.jeep.entity.Order;
import com.promineotech.jeep.entity.Tire;

@Component
public class DefaultJeepOrderDao implements JeepOrderDao {
  @Autowired
  private NamedParameterJdbcTemplate jdbcTemplate;  
 
  @Override
  public Optional<Jeep> fetchModel(JeepModel model, String trim, String trim2, int doors) {
    
    return null;
  }
  Order saveOrder(Customer customer, Jeep jeep, Color color, Engine engine, Tire
      tire, BigDecimal price, List<Option> options) {
    return null;
  }
   
  @Override
  public List<Option> fetchOptions(List<String> optionIds) {
    if(optionIds.isEmpty()) {
      return new LinkedList<>();
    }
    
    Map<String, Object> params =  new HashMap<>();
    
    String sql = ""
        + "SELECT * "
        + "FROM options "
        + "WHERE option_id IN(";
    
    for(int index = 0; index < optionIds.size(); index++) {
      String key = "option_" + index;
      sql += ":" + key + ", ";
      params.put(key, optionIds.get(index));
    }
    
    sql = sql.substring(0, sql.length() - 2);
    sql += ")";
    
    return jdbcTemplate.query(sql, params, new RowMapper<Option>() {

      @Override
      public Option mapRow(ResultSet rs, int rowNum) throws SQLException {
        
        return Option.builder()
            .category(OptionType.valueOf(rs.getString("category")))
            .manufacturer(rs.getString("manufacturer"))
            .name(rs.getString("name"))
            .optionId(rs.getString("option_id"))
            .optionPK(rs.getLong("option_pk"))
            .price(rs.getBigDecimal("price"))
            .build();
      }});
  }
  
  @Override
  public Optional<Customer> fetchCustomer(String customerId) {
    String sql = ""
        + "SELECT * "
        + "FROM customers "
        + "WHERE customer_id = :customer_id";
    
    Map<String, Object> params = new HashMap<>();
    params.put("customer_id", customerId);
    
    return Optional.ofNullable(
        jdbcTemplate.query(sql, params, new CustomerResultSetExtractor()));
  }
  
  public Optional<Jeep> fetchModel(JeepModel model, String trim, int doors) {
    String sql = ""
        + "SELECT * "
        + "FROM models "
        + "WHERE model_id = :model_id "
        + "AND trim_level = :trim_level "
        + "AND num_doors = :num_doors";
    
    Map<String, Object> params = new HashMap<>();
    params.put("model_id", model.toString());
    params.put("trim_level", trim);
    params.put("num_doors", doors);
    
    return Optional.ofNullable(
        jdbcTemplate.query(sql, params, new ModelResultSetExtractor()));
  }
  
  @Override
  public Optional<Color> fetchColor(String colorId) {
    String sql = ""
        + "SELECT * "
        + "FROM colors "
        + "WHERE color_id = :color_id";
    
    Map<String, Object> params = new HashMap<>();
    params.put("color_id", colorId);
    
    return Optional.ofNullable(jdbcTemplate.query(sql,  params, new ColorResultSetExtractor()));
  }
  
  @Override
  public Optional<Engine> fetchEngine(String engineId) {
    String sql = ""
        + "SELECT * "
        + "FROM engines "
        + "WHERE engine_id = :engine_id";
    
    Map<String, Object> params = new HashMap<>();
    params.put("engine_ID", engineId);
    
    return Optional.ofNullable(jdbcTemplate.query(sql, params, new EngineResultSetExtractor()));    
  }
  
  @Override
  public Optional<Tire> fetchTire(String tireId) {
    String sql = ""
        + "SELECT * "
        + "FROM tires "
        + "WHERE tire_id = :tire_id";
    
    Map<String, Object> params = new HashMap<>();
    params.put("tire_id", tireId);
    
    return Optional.ofNullable(jdbcTemplate.query(sql, params, new TireResultSetExtractor()));
  }
  
  class TireResultSetExtractor implements ResultSetExtractor<Tire> {
    @Override
    public Tire extractData(ResultSet rs) throws SQLException {
      rs.next();
      
      return Tire.builder()
          .tirePK(rs.getLong("tire_pk"))
          .tireId(rs.getString("tire_id"))
          .tireSize(rs.getString("tire_size"))
          .manufacturer(rs.getString("manufacturer"))
          .price(rs.getBigDecimal("price"))
          .warrantyMiles(rs.getInt("warranty_miles"))
          .build();
    }
  }
  
  class EngineResultSetExtractor implements ResultSetExtractor<Engine> {
    @Override
    public Engine extractData(ResultSet rs) throws SQLException {
      rs.next();
      
      return Engine.builder()
          .enginePK(rs.getLong("engine_pk"))
          .engineId(rs.getString("engine_id"))
          .sizeInLiters(rs.getFloat("size_in_liters"))
          .name(rs.getString("name"))
          .mpgCity(rs.getFloat("mpg_city"))
          .mpgHwy(rs.getFloat("mpg_hwy"))
          .hasStartStop(rs.getBoolean("has_start_stop"))
          .description(rs.getString("description"))
          .price(rs.getBigDecimal("price"))
          .build();
    }
  }
  
  class ColorResultSetExtractor implements ResultSetExtractor<Color> {
    @Override
    public Color extractData(ResultSet rs) throws SQLException {
      rs.next();
      
      return Color.builder()
          .colorPK(rs.getLong("color_pk"))
          .colorId(rs.getString("color_id"))
          .color(rs.getString("color"))
          .price(rs.getBigDecimal("price"))
          .isExterior(rs.getBoolean("is_exterior"))
          .build();
    }
  }
  
  class ModelResultSetExtractor implements ResultSetExtractor<Jeep> {
    @Override
    public Jeep extractData(ResultSet rs) 
        throws SQLException {
          rs.next();
        
      return Jeep.builder()
          .modelPK(rs.getLong("model_pk"))
          .trimLevel(rs.getString("trim_level"))
          .numDoors(rs.getInt("num_doors"))
          .wheelSize(rs.getInt("wheel_size"))
          .basePrice(rs.getBigDecimal("base_price"))
          .build();
     
    }
  }
  

  class CustomerResultSetExtractor implements ResultSetExtractor<Customer> {
    @Override
    public Customer extractData(ResultSet rs) 
        throws SQLException, DataAccessException {
      rs.next();
      
      return Customer.builder()
          .customerId(rs.getString("customer_id"))
          .customerPK(rs.getLong("customer_pk"))
          .firstName(rs.getString("first_name"))
          .lastName(rs.getString("last_name"))
          .phone(rs.getString("phone"))
          .build();
    }
   }


 


 



}
