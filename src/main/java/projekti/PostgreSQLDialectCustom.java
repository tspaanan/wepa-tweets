package projekti;

import org.hibernate.dialect.PostgreSQL82Dialect;
import org.hibernate.type.descriptor.sql.BinaryTypeDescriptor;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;
import org.springframework.context.annotation.Profile;

/**
 *
 * @author tspaanan
 */
@Profile("production")
//source: https://www.javacodegeeks.com/2014/03/postgres-and-oracle-compatibility-with-hibernate.html
public class PostgreSQLDialectCustom extends PostgreSQL82Dialect {
    
    @Override
    public SqlTypeDescriptor remapSqlTypeDescriptor(SqlTypeDescriptor sqlTypeDescriptor) {
    if (sqlTypeDescriptor.getSqlType() == java.sql.Types.BLOB) {
      return BinaryTypeDescriptor.INSTANCE;
    }
    return super.remapSqlTypeDescriptor(sqlTypeDescriptor);
  }
}
