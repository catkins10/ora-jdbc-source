package oracle.jdbc.driver;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import oracle.sql.ARRAY;
import oracle.sql.BFILE;
import oracle.sql.BLOB;
import oracle.sql.CHAR;
import oracle.sql.CLOB;
import oracle.sql.CustomDatum;
import oracle.sql.CustomDatumFactory;
import oracle.sql.DATE;
import oracle.sql.Datum;
import oracle.sql.INTERVALDS;
import oracle.sql.INTERVALYM;
import oracle.sql.NUMBER;
import oracle.sql.OPAQUE;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import oracle.sql.RAW;
import oracle.sql.REF;
import oracle.sql.ROWID;
import oracle.sql.STRUCT;
import oracle.sql.TIMESTAMP;
import oracle.sql.TIMESTAMPLTZ;

class OracleReturnResultSet extends BaseResultSet
{
  OracleStatement statement;
  boolean closed;
  Accessor[] returnAccessors;

  OracleReturnResultSet(OracleStatement paramOracleStatement)
    throws SQLException
  {
    this.statement = paramOracleStatement;
    this.closed = false;

    this.returnAccessors = new Accessor[paramOracleStatement.numReturnParams];

    int i = 0;
    for (int j = 0; j < paramOracleStatement.numberOfBindPositions; j++)
    {
      Accessor localAccessor = paramOracleStatement.returnParamAccessors[j];

      if (localAccessor != null)
        this.returnAccessors[(i++)] = localAccessor;
    }
  }

  public synchronized boolean next()
    throws SQLException
  {
    if (this.closed) return false;

    if (!this.statement.returnParamsFetched)
    {
      this.statement.fetchDmlReturnParams();
      this.statement.setupReturnParamAccessors();
    }

    this.statement.currentRow += 1;
    this.statement.totalRowsVisited += 1;

    return this.statement.currentRow < this.statement.rowsDmlReturned;
  }

  public synchronized void close()
    throws SQLException
  {
    this.closed = true;

    this.statement.returnResultSet = null;
    this.statement.numReturnParams = 0;
    this.statement.totalRowsVisited = 0;
    this.statement.currentRow = -1;
    this.statement.returnParamsFetched = false;
    this.statement.rowsDmlReturned = 0;
    this.statement.returnParamBytes = null;
    this.statement.returnParamChars = null;
    this.statement.returnParamIndicators = null;
  }

  public synchronized boolean wasNull()
    throws SQLException
  {
    if (this.closed) {
      DatabaseError.throwSqlException(11);
    }
    if ((this.statement.currentRow == -1) || (this.statement.lastIndex == 0)) {
      DatabaseError.throwSqlException(24);
    }
    return this.returnAccessors[(this.statement.lastIndex - 1)].isNull(this.statement.currentRow);
  }

  public synchronized ResultSetMetaData getMetaData()
    throws SQLException
  {
    if (!this.statement.isAutoGeneratedKey) {
      DatabaseError.throwSqlException(23);
    }
    if (this.closed) {
      DatabaseError.throwSqlException(10);
    }
    if (this.statement.closed) {
      DatabaseError.throwSqlException(9);
    }
    AutoKeyInfo localAutoKeyInfo = this.statement.autoKeyInfo;
    localAutoKeyInfo.statement = this.statement;
    localAutoKeyInfo.connection = this.statement.connection;
    localAutoKeyInfo.initMetaData(this);
    return localAutoKeyInfo;
  }

  public synchronized Statement getStatement()
    throws SQLException
  {
    return this.statement;
  }

  public synchronized String getString(int paramInt)
    throws SQLException
  {
    if (this.closed) {
      DatabaseError.throwSqlException(11);
    }
    if ((paramInt <= 0) || (paramInt > this.statement.numReturnParams)) {
      DatabaseError.throwSqlException(3);
    }
    int i = this.statement.currentRow;
    if (i < 0) {
      DatabaseError.throwSqlException(14);
    }

    this.statement.lastIndex = paramInt;

    return this.returnAccessors[(paramInt - 1)].getString(i);
  }

  public synchronized boolean getBoolean(int paramInt)
    throws SQLException
  {
    if (this.closed) {
      DatabaseError.throwSqlException(11);
    }
    if ((paramInt <= 0) || (paramInt > this.statement.numReturnParams)) {
      DatabaseError.throwSqlException(3);
    }
    int i = this.statement.currentRow;
    if (i < 0) {
      DatabaseError.throwSqlException(14);
    }

    this.statement.lastIndex = paramInt;

    return this.returnAccessors[(paramInt - 1)].getBoolean(i);
  }

  public synchronized byte getByte(int paramInt)
    throws SQLException
  {
    if (this.closed) {
      DatabaseError.throwSqlException(11);
    }
    if ((paramInt <= 0) || (paramInt > this.statement.numReturnParams)) {
      DatabaseError.throwSqlException(3);
    }
    int i = this.statement.currentRow;
    if (i < 0) {
      DatabaseError.throwSqlException(14);
    }

    this.statement.lastIndex = paramInt;

    return this.returnAccessors[(paramInt - 1)].getByte(i);
  }

  public synchronized short getShort(int paramInt)
    throws SQLException
  {
    if (this.closed) {
      DatabaseError.throwSqlException(11);
    }
    if ((paramInt <= 0) || (paramInt > this.statement.numReturnParams)) {
      DatabaseError.throwSqlException(3);
    }
    int i = this.statement.currentRow;
    if (i < 0) {
      DatabaseError.throwSqlException(14);
    }

    this.statement.lastIndex = paramInt;

    return this.returnAccessors[(paramInt - 1)].getShort(i);
  }

  public synchronized int getInt(int paramInt)
    throws SQLException
  {
    if (this.closed) {
      DatabaseError.throwSqlException(11);
    }
    if ((paramInt <= 0) || (paramInt > this.statement.numReturnParams)) {
      DatabaseError.throwSqlException(3);
    }
    int i = this.statement.currentRow;
    if (i < 0) {
      DatabaseError.throwSqlException(14);
    }

    this.statement.lastIndex = paramInt;

    return this.returnAccessors[(paramInt - 1)].getInt(i);
  }

  public synchronized long getLong(int paramInt)
    throws SQLException
  {
    if (this.closed) {
      DatabaseError.throwSqlException(11);
    }
    if ((paramInt <= 0) || (paramInt > this.statement.numReturnParams)) {
      DatabaseError.throwSqlException(3);
    }
    int i = this.statement.currentRow;
    if (i < 0) {
      DatabaseError.throwSqlException(14);
    }

    this.statement.lastIndex = paramInt;

    return this.returnAccessors[(paramInt - 1)].getLong(i);
  }

  public synchronized float getFloat(int paramInt)
    throws SQLException
  {
    if (this.closed) {
      DatabaseError.throwSqlException(11);
    }
    if ((paramInt <= 0) || (paramInt > this.statement.numReturnParams)) {
      DatabaseError.throwSqlException(3);
    }
    int i = this.statement.currentRow;
    if (i < 0) {
      DatabaseError.throwSqlException(14);
    }

    this.statement.lastIndex = paramInt;

    return this.returnAccessors[(paramInt - 1)].getFloat(i);
  }

  public synchronized double getDouble(int paramInt)
    throws SQLException
  {
    if (this.closed) {
      DatabaseError.throwSqlException(11);
    }
    if ((paramInt <= 0) || (paramInt > this.statement.numReturnParams)) {
      DatabaseError.throwSqlException(3);
    }
    int i = this.statement.currentRow;
    if (i < 0) {
      DatabaseError.throwSqlException(14);
    }

    this.statement.lastIndex = paramInt;

    return this.returnAccessors[(paramInt - 1)].getDouble(i);
  }

  public synchronized BigDecimal getBigDecimal(int paramInt1, int paramInt2)
    throws SQLException
  {
    if (this.closed) {
      DatabaseError.throwSqlException(11);
    }
    if ((paramInt1 <= 0) || (paramInt1 > this.statement.numReturnParams)) {
      DatabaseError.throwSqlException(3);
    }
    int i = this.statement.currentRow;
    if (i < 0) {
      DatabaseError.throwSqlException(14);
    }

    this.statement.lastIndex = paramInt1;

    return this.returnAccessors[(paramInt1 - 1)].getBigDecimal(i, paramInt2);
  }

  public synchronized byte[] getBytes(int paramInt)
    throws SQLException
  {
    if (this.closed) {
      DatabaseError.throwSqlException(11);
    }
    if ((paramInt <= 0) || (paramInt > this.statement.numReturnParams)) {
      DatabaseError.throwSqlException(3);
    }
    int i = this.statement.currentRow;
    if (i < 0) {
      DatabaseError.throwSqlException(14);
    }

    this.statement.lastIndex = paramInt;

    return this.returnAccessors[(paramInt - 1)].getBytes(i);
  }

  public synchronized Date getDate(int paramInt)
    throws SQLException
  {
    if (this.closed) {
      DatabaseError.throwSqlException(11);
    }
    if ((paramInt <= 0) || (paramInt > this.statement.numReturnParams)) {
      DatabaseError.throwSqlException(3);
    }
    int i = this.statement.currentRow;
    if (i < 0) {
      DatabaseError.throwSqlException(14);
    }

    this.statement.lastIndex = paramInt;

    return this.returnAccessors[(paramInt - 1)].getDate(i);
  }

  public synchronized Time getTime(int paramInt)
    throws SQLException
  {
    if (this.closed) {
      DatabaseError.throwSqlException(11);
    }
    if ((paramInt <= 0) || (paramInt > this.statement.numReturnParams)) {
      DatabaseError.throwSqlException(3);
    }
    int i = this.statement.currentRow;
    if (i < 0) {
      DatabaseError.throwSqlException(14);
    }

    this.statement.lastIndex = paramInt;

    return this.returnAccessors[(paramInt - 1)].getTime(i);
  }

  public synchronized Timestamp getTimestamp(int paramInt)
    throws SQLException
  {
    if (this.closed) {
      DatabaseError.throwSqlException(11);
    }
    if ((paramInt <= 0) || (paramInt > this.statement.numReturnParams)) {
      DatabaseError.throwSqlException(3);
    }
    int i = this.statement.currentRow;
    if (i < 0) {
      DatabaseError.throwSqlException(14);
    }

    this.statement.lastIndex = paramInt;

    return this.returnAccessors[(paramInt - 1)].getTimestamp(i);
  }

  public synchronized InputStream getAsciiStream(int paramInt)
    throws SQLException
  {
    DatabaseError.throwSqlException(23);
    return null;
  }

  public synchronized InputStream getUnicodeStream(int paramInt)
    throws SQLException
  {
    DatabaseError.throwSqlException(23);
    return null;
  }

  public synchronized InputStream getBinaryStream(int paramInt)
    throws SQLException
  {
    DatabaseError.throwSqlException(23);
    return null;
  }

  public synchronized Object getObject(int paramInt)
    throws SQLException
  {
    if (this.closed) {
      DatabaseError.throwSqlException(11);
    }
    if ((paramInt <= 0) || (paramInt > this.statement.numReturnParams)) {
      DatabaseError.throwSqlException(3);
    }
    int i = this.statement.currentRow;
    if (i < 0) {
      DatabaseError.throwSqlException(14);
    }

    this.statement.lastIndex = paramInt;

    return this.returnAccessors[(paramInt - 1)].getObject(i);
  }

  public synchronized ResultSet getCursor(int paramInt)
    throws SQLException
  {
    DatabaseError.throwSqlException(23);
    return null;
  }

  public synchronized Datum getOracleObject(int paramInt)
    throws SQLException
  {
    if (this.closed) {
      DatabaseError.throwSqlException(11);
    }
    if ((paramInt <= 0) || (paramInt > this.statement.numReturnParams)) {
      DatabaseError.throwSqlException(3);
    }
    int i = this.statement.currentRow;
    if (i < 0) {
      DatabaseError.throwSqlException(14);
    }

    this.statement.lastIndex = paramInt;

    return this.returnAccessors[(paramInt - 1)].getOracleObject(i);
  }

  public synchronized ROWID getROWID(int paramInt)
    throws SQLException
  {
    if (this.closed) {
      DatabaseError.throwSqlException(11);
    }
    if ((paramInt <= 0) || (paramInt > this.statement.numReturnParams)) {
      DatabaseError.throwSqlException(3);
    }
    int i = this.statement.currentRow;
    if (i < 0) {
      DatabaseError.throwSqlException(14);
    }

    this.statement.lastIndex = paramInt;

    return this.returnAccessors[(paramInt - 1)].getROWID(i);
  }

  public synchronized NUMBER getNUMBER(int paramInt)
    throws SQLException
  {
    if (this.closed) {
      DatabaseError.throwSqlException(11);
    }
    if ((paramInt <= 0) || (paramInt > this.statement.numReturnParams)) {
      DatabaseError.throwSqlException(3);
    }
    int i = this.statement.currentRow;
    if (i < 0) {
      DatabaseError.throwSqlException(14);
    }

    this.statement.lastIndex = paramInt;

    return this.returnAccessors[(paramInt - 1)].getNUMBER(i);
  }

  public synchronized DATE getDATE(int paramInt)
    throws SQLException
  {
    if (this.closed) {
      DatabaseError.throwSqlException(11);
    }
    if ((paramInt <= 0) || (paramInt > this.statement.numReturnParams)) {
      DatabaseError.throwSqlException(3);
    }
    int i = this.statement.currentRow;
    if (i < 0) {
      DatabaseError.throwSqlException(14);
    }

    this.statement.lastIndex = paramInt;

    return this.returnAccessors[(paramInt - 1)].getDATE(i);
  }

  public synchronized ARRAY getARRAY(int paramInt)
    throws SQLException
  {
    if (this.closed) {
      DatabaseError.throwSqlException(11);
    }
    if ((paramInt <= 0) || (paramInt > this.statement.numReturnParams)) {
      DatabaseError.throwSqlException(3);
    }
    int i = this.statement.currentRow;
    if (i < 0) {
      DatabaseError.throwSqlException(14);
    }

    this.statement.lastIndex = paramInt;

    return this.returnAccessors[(paramInt - 1)].getARRAY(i);
  }

  public synchronized STRUCT getSTRUCT(int paramInt)
    throws SQLException
  {
    if (this.closed) {
      DatabaseError.throwSqlException(11);
    }
    if ((paramInt <= 0) || (paramInt > this.statement.numReturnParams)) {
      DatabaseError.throwSqlException(3);
    }
    int i = this.statement.currentRow;
    if (i < 0) {
      DatabaseError.throwSqlException(14);
    }

    this.statement.lastIndex = paramInt;

    return this.returnAccessors[(paramInt - 1)].getSTRUCT(i);
  }

  public synchronized OPAQUE getOPAQUE(int paramInt)
    throws SQLException
  {
    DatabaseError.throwSqlException(23);
    return null;
  }

  public synchronized REF getREF(int paramInt)
    throws SQLException
  {
    if (this.closed) {
      DatabaseError.throwSqlException(11);
    }
    if ((paramInt <= 0) || (paramInt > this.statement.numReturnParams)) {
      DatabaseError.throwSqlException(3);
    }
    int i = this.statement.currentRow;
    if (i < 0) {
      DatabaseError.throwSqlException(14);
    }

    this.statement.lastIndex = paramInt;

    return this.returnAccessors[(paramInt - 1)].getREF(i);
  }

  public synchronized CHAR getCHAR(int paramInt)
    throws SQLException
  {
    if (this.closed) {
      DatabaseError.throwSqlException(11);
    }
    if ((paramInt <= 0) || (paramInt > this.statement.numReturnParams)) {
      DatabaseError.throwSqlException(3);
    }
    int i = this.statement.currentRow;
    if (i < 0) {
      DatabaseError.throwSqlException(14);
    }

    this.statement.lastIndex = paramInt;

    return this.returnAccessors[(paramInt - 1)].getCHAR(i);
  }

  public synchronized RAW getRAW(int paramInt)
    throws SQLException
  {
    if (this.closed) {
      DatabaseError.throwSqlException(11);
    }
    if ((paramInt <= 0) || (paramInt > this.statement.numReturnParams)) {
      DatabaseError.throwSqlException(3);
    }
    int i = this.statement.currentRow;
    if (i < 0) {
      DatabaseError.throwSqlException(14);
    }

    this.statement.lastIndex = paramInt;

    return this.returnAccessors[(paramInt - 1)].getRAW(i);
  }

  public synchronized BLOB getBLOB(int paramInt)
    throws SQLException
  {
    if (this.closed) {
      DatabaseError.throwSqlException(11);
    }
    if ((paramInt <= 0) || (paramInt > this.statement.numReturnParams)) {
      DatabaseError.throwSqlException(3);
    }
    int i = this.statement.currentRow;
    if (i < 0) {
      DatabaseError.throwSqlException(14);
    }

    this.statement.lastIndex = paramInt;

    return this.returnAccessors[(paramInt - 1)].getBLOB(i);
  }

  public synchronized CLOB getCLOB(int paramInt)
    throws SQLException
  {
    if (this.closed) {
      DatabaseError.throwSqlException(11);
    }
    if ((paramInt <= 0) || (paramInt > this.statement.numReturnParams)) {
      DatabaseError.throwSqlException(3);
    }
    int i = this.statement.currentRow;
    if (i < 0) {
      DatabaseError.throwSqlException(14);
    }

    this.statement.lastIndex = paramInt;

    return this.returnAccessors[(paramInt - 1)].getCLOB(i);
  }

  public synchronized BFILE getBFILE(int paramInt)
    throws SQLException
  {
    if (this.closed) {
      DatabaseError.throwSqlException(11);
    }
    if ((paramInt <= 0) || (paramInt > this.statement.numReturnParams)) {
      DatabaseError.throwSqlException(3);
    }
    int i = this.statement.currentRow;
    if (i < 0) {
      DatabaseError.throwSqlException(14);
    }

    this.statement.lastIndex = paramInt;

    return this.returnAccessors[(paramInt - 1)].getBFILE(i);
  }

  public synchronized BFILE getBfile(int paramInt)
    throws SQLException
  {
    return getBFILE(paramInt);
  }

  public synchronized CustomDatum getCustomDatum(int paramInt, CustomDatumFactory paramCustomDatumFactory)
    throws SQLException
  {
    DatabaseError.throwSqlException(23);
    return null;
  }

  public synchronized ORAData getORAData(int paramInt, ORADataFactory paramORADataFactory)
    throws SQLException
  {
    if (this.closed) {
      DatabaseError.throwSqlException(11);
    }
    if ((paramInt <= 0) || (paramInt > this.statement.numReturnParams)) {
      DatabaseError.throwSqlException(3);
    }
    int i = this.statement.currentRow;
    if (i < 0) {
      DatabaseError.throwSqlException(14);
    }

    this.statement.lastIndex = paramInt;

    return this.returnAccessors[(paramInt - 1)].getORAData(i, paramORADataFactory);
  }

  public synchronized Object getObject(int paramInt, Map paramMap)
    throws SQLException
  {
    if (this.closed) {
      DatabaseError.throwSqlException(11);
    }
    if ((paramInt <= 0) || (paramInt > this.statement.numReturnParams)) {
      DatabaseError.throwSqlException(3);
    }
    int i = this.statement.currentRow;
    if (i < 0) {
      DatabaseError.throwSqlException(14);
    }

    this.statement.lastIndex = paramInt;

    return this.returnAccessors[(paramInt - 1)].getObject(i, paramMap);
  }

  public synchronized Ref getRef(int paramInt)
    throws SQLException
  {
    return getREF(paramInt);
  }

  public synchronized Blob getBlob(int paramInt)
    throws SQLException
  {
    return getBLOB(paramInt);
  }

  public synchronized Clob getClob(int paramInt)
    throws SQLException
  {
    return getCLOB(paramInt);
  }

  public synchronized Array getArray(int paramInt)
    throws SQLException
  {
    DatabaseError.throwSqlException(23);
    return null;
  }

  public synchronized Reader getCharacterStream(int paramInt)
    throws SQLException
  {
    DatabaseError.throwSqlException(23);
    return null;
  }

  public BigDecimal getBigDecimal(int paramInt)
    throws SQLException
  {
    if (this.closed) {
      DatabaseError.throwSqlException(11);
    }
    if ((paramInt <= 0) || (paramInt > this.statement.numReturnParams)) {
      DatabaseError.throwSqlException(3);
    }
    int i = this.statement.currentRow;
    if (i < 0) {
      DatabaseError.throwSqlException(14);
    }

    this.statement.lastIndex = paramInt;

    return this.returnAccessors[(paramInt - 1)].getBigDecimal(i);
  }

  public Date getDate(int paramInt, Calendar paramCalendar)
    throws SQLException
  {
    if (this.closed) {
      DatabaseError.throwSqlException(11);
    }
    if ((paramInt <= 0) || (paramInt > this.statement.numReturnParams)) {
      DatabaseError.throwSqlException(3);
    }
    int i = this.statement.currentRow;
    if (i < 0) {
      DatabaseError.throwSqlException(14);
    }

    this.statement.lastIndex = paramInt;

    return this.returnAccessors[(paramInt - 1)].getDate(i, paramCalendar);
  }

  public Time getTime(int paramInt, Calendar paramCalendar)
    throws SQLException
  {
    if (this.closed) {
      DatabaseError.throwSqlException(11);
    }
    if ((paramInt <= 0) || (paramInt > this.statement.numReturnParams)) {
      DatabaseError.throwSqlException(3);
    }
    int i = this.statement.currentRow;
    if (i < 0) {
      DatabaseError.throwSqlException(14);
    }

    this.statement.lastIndex = paramInt;

    return this.returnAccessors[(paramInt - 1)].getTime(i, paramCalendar);
  }

  public Timestamp getTimestamp(int paramInt, Calendar paramCalendar)
    throws SQLException
  {
    if (this.closed) {
      DatabaseError.throwSqlException(11);
    }
    if ((paramInt <= 0) || (paramInt > this.statement.numReturnParams)) {
      DatabaseError.throwSqlException(3);
    }
    int i = this.statement.currentRow;
    if (i < 0) {
      DatabaseError.throwSqlException(14);
    }

    this.statement.lastIndex = paramInt;

    return this.returnAccessors[(paramInt - 1)].getTimestamp(i, paramCalendar);
  }

  public INTERVALYM getINTERVALYM(int paramInt)
    throws SQLException
  {
    if (this.closed) {
      DatabaseError.throwSqlException(11);
    }
    if ((paramInt <= 0) || (paramInt > this.statement.numReturnParams)) {
      DatabaseError.throwSqlException(3);
    }
    int i = this.statement.currentRow;
    if (i < 0) {
      DatabaseError.throwSqlException(14);
    }

    this.statement.lastIndex = paramInt;

    return this.returnAccessors[(paramInt - 1)].getINTERVALYM(i);
  }

  public INTERVALDS getINTERVALDS(int paramInt)
    throws SQLException
  {
    if (this.closed) {
      DatabaseError.throwSqlException(11);
    }
    if ((paramInt <= 0) || (paramInt > this.statement.numReturnParams)) {
      DatabaseError.throwSqlException(3);
    }
    int i = this.statement.currentRow;
    if (i < 0) {
      DatabaseError.throwSqlException(14);
    }

    this.statement.lastIndex = paramInt;

    return this.returnAccessors[(paramInt - 1)].getINTERVALDS(i);
  }

  public TIMESTAMP getTIMESTAMP(int paramInt)
    throws SQLException
  {
    if (this.closed) {
      DatabaseError.throwSqlException(11);
    }
    if ((paramInt <= 0) || (paramInt > this.statement.numReturnParams)) {
      DatabaseError.throwSqlException(3);
    }
    int i = this.statement.currentRow;
    if (i < 0) {
      DatabaseError.throwSqlException(14);
    }

    this.statement.lastIndex = paramInt;

    return this.returnAccessors[(paramInt - 1)].getTIMESTAMP(i);
  }

  public TIMESTAMPLTZ getTIMESTAMPLTZ(int paramInt)
    throws SQLException
  {
    if (this.closed) {
      DatabaseError.throwSqlException(11);
    }
    if ((paramInt <= 0) || (paramInt > this.statement.numReturnParams)) {
      DatabaseError.throwSqlException(3);
    }
    int i = this.statement.currentRow;
    if (i < 0) {
      DatabaseError.throwSqlException(14);
    }

    this.statement.lastIndex = paramInt;

    return this.returnAccessors[(paramInt - 1)].getTIMESTAMPLTZ(i);
  }

  public synchronized URL getURL(int paramInt)
    throws SQLException
  {
    if (this.closed) {
      DatabaseError.throwSqlException(11);
    }
    if ((paramInt <= 0) || (paramInt > this.statement.numReturnParams)) {
      DatabaseError.throwSqlException(3);
    }
    int i = this.statement.currentRow;
    if (i < 0) {
      DatabaseError.throwSqlException(14);
    }

    this.statement.lastIndex = paramInt;

    return this.returnAccessors[(paramInt - 1)].getURL(i);
  }

  public boolean isBeforeFirst()
    throws SQLException
  {
    return (!isEmptyResultSet()) && (this.statement.currentRow == -1) && (!this.closed);
  }

  public boolean isAfterLast()
    throws SQLException
  {
    return (!isEmptyResultSet()) && (this.closed);
  }

  public boolean isFirst()
    throws SQLException
  {
    return getRow() == 1;
  }

  public boolean isLast()
    throws SQLException
  {
    return getRow() == this.statement.rowsDmlReturned;
  }

  public int getRow()
    throws SQLException
  {
    return this.statement.totalRowsVisited;
  }

  public synchronized int findColumn(String paramString)
    throws SQLException
  {
    DatabaseError.throwSqlException(23);
    return -1;
  }

  public void setFetchSize(int paramInt)
    throws SQLException
  {
    DatabaseError.throwSqlException(23);
  }

  public int getFetchSize()
    throws SQLException
  {
    DatabaseError.throwSqlException(23);
    return -1;
  }

  boolean isEmptyResultSet()
  {
    return this.statement.rowsDmlReturned == 0;
  }
}

/* Location:           D:\oracle\product\10.2.0\client_1\jdbc\lib\ojdbc14.jar
 * Qualified Name:     oracle.jdbc.driver.OracleReturnResultSet
 * JD-Core Version:    0.6.0
 */