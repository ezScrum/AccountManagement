package SQLService;

public interface  QuerySet {
	final public static String ASC_ORDER = "ASC";
	final public static String DESC_ORDER = "DESC";	
	
	public void addResultRow(String rowName);
	public void addInsertValue(QueryValue value);
	
	public void addTableName(String tableName);
	public void joinTableName(String joinName,String on); //��join�覡��sTable
	
	public void addInsertValue(String name, String value);
	public void addInsertValue(String name, long value);
	
	// Left Join by Field
	public void addLeftJoin(String tableName, String name1, String name2);
	public void addNotNullCondition(String name);
	public void addEqualCondition(String name, String value);//���P�ƭ�
	public void addEqualCondition(String name, long value);//���P�ƭ�
	public void addLessCondition(String name, String value);//���P�ƭ�
	public void addLessOrEqualCondition(String name, long value);//���P�ƭ�
	public void addBigCondition(String name, String value);//���P�ƭ�
	public void addFieldEqualCondition(String name1, String name2);//���P���
	public void addLikeCondition(String name, String value);//���P�r��
	public void addTextFieldEqualCondition(String name, String value);
	
	public void addLikeConditionXtable(String name, String value);//���P�r��

	public String getColumns();

	public String getColumnValues();

	public String getConditions();

	public String getTableNames();
	
	public String getLeftJoins();
	
	public String getColumnsAndValues();
	
	public String getSelectQuery();
	
	public String getInsertQuery();
	
	public String getUpdateQuery();
	
	public String getDeleteQuery();
	//Kanban �b�ϥ�
	public String getMaxQuery(String tableName, String column);
	
	public void setOrderBy(String name, String desc);
	
	public void clear();
}
