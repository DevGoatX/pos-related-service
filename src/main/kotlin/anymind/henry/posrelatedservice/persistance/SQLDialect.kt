package anymind.henry.posrelatedservice.persistance

import org.hibernate.dialect.Dialect
import org.hibernate.dialect.function.StandardSQLFunction
import org.hibernate.dialect.function.SQLFunctionTemplate
import org.hibernate.dialect.function.VarArgsSQLFunction
import org.hibernate.Hibernate
import org.hibernate.type.StringType
import java.sql.Types

class SQLDialect : Dialect() {
    fun supportsIdentityColumns() = true
    fun hasDataTypeInIdentityColumn() = false

    // return "integer primary key autoincrement";
    fun getIdentityColumnString() = "integer"
    fun getIdentitySelectString() = "select last_insert_rowid()"

    override fun supportsLimit() = true

    override fun getLimitString(query: String, hasOffset: Boolean): String {
        return StringBuffer(query.length + 20).append(query).append(if (hasOffset) " limit ? offset ?" else " limit ?")
            .toString()
    }

    fun supportsTemporaryTables() = true
    val getCreateTemporaryTableString = "create temporary table if not exists"
    fun dropTemporaryTableAfterUse() = false
    override fun supportsCurrentTimestampSelection() = true
    override fun isCurrentTimestampSelectStringCallable() = false
    override fun getCurrentTimestampSelectString() = "select current_timestamp"
    override fun supportsUnionAll() = true
    override fun hasAlterTable() = false
    override fun dropConstraints() = false
    override fun getAddColumnString() = "add column"
    override fun getForUpdateString() = ""
    override fun supportsOuterJoinForUpdate() = false

    override fun getDropForeignKeyString() = throw UnsupportedOperationException("No drop foreign key syntax supported by SQLiteDialect")
    override fun getAddForeignKeyConstraintString(
        constraintName: String?, foreignKey: Array<String?>?, referencedTable: String?,
        primaryKey: Array<String?>?, referencesPrimaryKey: Boolean
    ): String {
        throw UnsupportedOperationException("No add foreign key syntax supported by SQLiteDialect")
    }

    override fun getAddPrimaryKeyConstraintString(constraintName: String?) = throw UnsupportedOperationException("No add primary key syntax supported by SQLiteDialect")

    override fun supportsIfExistsBeforeTableName() = true

    override fun supportsCascadeDelete() = false

    init {
        registerColumnType(Types.BIT, "integer")
        registerColumnType(Types.TINYINT, "tinyint")
        registerColumnType(Types.SMALLINT, "smallint")
        registerColumnType(Types.INTEGER, "integer")
        registerColumnType(Types.BIGINT, "bigint")
        registerColumnType(Types.FLOAT, "float")
        registerColumnType(Types.REAL, "real")
        registerColumnType(Types.DOUBLE, "double")
        registerColumnType(Types.NUMERIC, "numeric")
        registerColumnType(Types.DECIMAL, "decimal")
        registerColumnType(Types.CHAR, "char")
        registerColumnType(Types.VARCHAR, "varchar")
        registerColumnType(Types.LONGVARCHAR, "longvarchar")
        registerColumnType(Types.DATE, "date")
        registerColumnType(Types.TIME, "time")
        registerColumnType(Types.TIMESTAMP, "timestamp")
        registerColumnType(Types.BINARY, "blob")
        registerColumnType(Types.VARBINARY, "blob")
        registerColumnType(Types.LONGVARBINARY, "blob")
        // registerColumnType(Types.NULL, "null");
        registerColumnType(Types.BLOB, "blob")
        registerColumnType(Types.CLOB, "clob")
        registerColumnType(Types.BOOLEAN, "integer")
        registerFunction("concat", VarArgsSQLFunction(StringType.INSTANCE, "", "||", ""))
        registerFunction("mod", SQLFunctionTemplate(StringType.INSTANCE, "?1 % ?2"))
        registerFunction("substr", StandardSQLFunction("substr", StringType.INSTANCE))
        registerFunction("substring", StandardSQLFunction("substr", StringType.INSTANCE))
    }
}