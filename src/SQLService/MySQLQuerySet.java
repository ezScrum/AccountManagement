package SQLService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MySQLQuerySet implements QuerySet{
	List<QueryValue> _list;
	List<String> _conditionList;
	List<String> _tableList;
	List<String> _leftJoinList;
	List<String> _crossJoinList;
	List<String> _rowList;
	String _orderBy = "";
	String _orderDirection = ASC_ORDER;

	public MySQLQuerySet() {
		_list = new ArrayList<QueryValue>();
		_conditionList = new ArrayList<String>();
		_tableList = new ArrayList<String>();
		_leftJoinList = new ArrayList<String>();
		_crossJoinList = new ArrayList<String>();
		_rowList = new ArrayList<String>();
	}
	
	@Override
	public void addResultRow(String rowName) {
		_rowList.add(rowName);		
	}

	@Override
	public void addInsertValue(QueryValue value) {
		_list.add(value);		
	}

	@Override
	public void addTableName(String tableName) {
		this._tableList.add("`" + tableName + "`");
	}

	@Override
	public void joinTableName(String joinName, String on) {
		this._tableList.add(joinName + " on " + on);
	}

	@Override
	public void addInsertValue(String name, String value) {
		this.addInsertValue(new QueryValue(name, format(value)));
	}

	@Override
	public void addInsertValue(String name, long value) {
		addInsertValue(name, String.valueOf(value));
	}

	@Override
	public void addLeftJoin(String tableName, String name1, String name2) {
		this._leftJoinList.add("`" + tableName + "` ON " + name1 + " = " + name2);
	}

	@Override
	public void addNotNullCondition(String name) {
		if (name.contains(".")) name = name.replace(".", "`.`");
		this._conditionList.add("`" + name + "` IS NOT NULL");
	}

	@Override
	public void addEqualCondition(String name, String value) {
		if (name.contains(".")) name = name.replace(".", "`.`");
		this._conditionList.add("`" + name + "` = " + value);
	}

	@Override
	public void addEqualCondition(String name, long value) {
		addEqualCondition(name, String.valueOf(value));
	}

	@Override
	public void addLessCondition(String name, String value) {
		if (name.contains(".")) name = name.replace(".", "`.`");
		this._conditionList.add("`" + name + "` < " + value);
	}

	@Override
	public void addLessOrEqualCondition(String name, long value) {
		if (name.contains(".")) name = name.replace(".", "`.`");
		this._conditionList.add("`" + name + "` <= " + value);
	}

	@Override
	public void addBigCondition(String name, String value) {
		if (name.contains(".")) name = name.replace(".", "`.`");
		this._conditionList.add("`" + name + "` > " + value);
	}

	@Override
	public void addFieldEqualCondition(String name1, String name2) {
		this._conditionList.add(name1 + " = " + name2);
	}

	@Override
	public void addLikeCondition(String name, String value) {
		this._conditionList.add("`" + name + "` LIKE '" + value + "'");
	}

	@Override
	public void addTextFieldEqualCondition(String name, String value) {
		if (name.contains(".")) name = name.replace(".", "`.`");
		this._conditionList.add("`" + name + "` = '" + format(value) + "'");
	}

	@Override
	public void addLikeConditionXtable(String name, String value) {
		this._conditionList.add(name + " LIKE '" + value + "'");
	}

	@Override
	public String getColumns() {
		StringBuffer columns = new StringBuffer();
		Iterator<QueryValue> iter = _list.iterator();
		if (_list.size() == 0) {
			return columns.toString();
		} else {
			columns.append(iter.next().getName());
		}
		while (iter.hasNext()) {
			columns.append(", " + iter.next().getName());
		}
		return columns.toString();
	}

	@Override
	public String getColumnValues() {
		StringBuffer columnValues = new StringBuffer();
		Iterator<QueryValue> iter = _list.iterator();
		if (_list.size() == 0) {
			return columnValues.toString();
		} else {
			columnValues.append(iter.next().getValue());
		}
		while (iter.hasNext()) {
			columnValues.append(", " + iter.next().getValue());
		}
		return columnValues.toString();
	}

	@Override
	public String getConditions() {
		StringBuffer conditions = new StringBuffer();
		Iterator<String> iter = _conditionList.iterator();
		if (_conditionList.size() == 0) {
			return conditions.toString();
		} else {
			conditions.append(iter.next());
		}
		while (iter.hasNext()) {
			conditions.append(" AND " + iter.next());
		}

		return conditions.toString();
	}

	@Override
	public String getTableNames() {
		StringBuffer tableNames = new StringBuffer();		
		Iterator<String> iter = _tableList.iterator();
		if (_tableList.size() == 0) {
			return tableNames.toString();
		} else {
			tableNames.append(iter.next());
		}
		while (iter.hasNext()) {
			tableNames.append(" join " + iter.next());
		}
		return tableNames.toString();
	}

	@Override
	public String getLeftJoins() {
		StringBuffer leftJoins = new StringBuffer();
		Iterator<String> iter = _leftJoinList.iterator();
		if (_leftJoinList.size() == 0) {
			return leftJoins.toString();
		} else {
			leftJoins.append(iter.next());
		}
		while (iter.hasNext()) {
			leftJoins.append(" Left Join " + iter.next());
		}

		return leftJoins.toString();
	}

	@Override
	public String getColumnsAndValues() {
		StringBuffer modifyValue = new StringBuffer();
		Iterator<QueryValue> iter = _list.iterator();
		if (_list.size() == 0) {
			return modifyValue.toString();
		} else {
			QueryValue value = iter.next();
			modifyValue.append(value.getName() + " = " + value.getValue());
		}
		while (iter.hasNext()) {
			QueryValue value = iter.next();
			modifyValue.append(", " + value.getName() + " = "
			        + value.getValue());
		}

		return modifyValue.toString();
	}

	@Override
	public String getSelectQuery() {
		StringBuffer query = new StringBuffer();
		if (_rowList.isEmpty())	{
			query.append("SELECT * FROM " + getTableNames());
		}else {
			query.append("SELECT ");
			for (String rowName : _rowList)	{
				query.append(rowName + ",");
			}
			query.deleteCharAt(query.length() - 1);
			query.append(" FROM " + getTableNames());
		}

		if (this._leftJoinList.size() != 0) query.append(" LEFT JOIN" + getLeftJoins());
		if (this._crossJoinList.size() != 0) query.append(" CROSS JOIN" + getCrossJoins());
		if (this._conditionList.size() != 0) query.append(" WHERE " + getConditions());
		if (!this._orderBy.equals("")) query.append(" ORDER BY `" + this._orderBy + "` " + this._orderDirection);
		return query.toString();
	}

	@Override
	public String getInsertQuery() {
		return "INSERT INTO " + getTableNames() + " ( " + this.getColumns() + " ) VALUES ( " + this.getColumnValues() + " )";
	}

	@Override
	public String getUpdateQuery() {
		return "UPDATE " + this.getTableNames() + " SET " + this.getColumnsAndValues() + " WHERE " + this.getConditions();
	}

	@Override
	public String getDeleteQuery() {
		return "DELETE FROM " + this.getTableNames() + " WHERE " + this.getConditions();
	}

	@Override
	public String getMaxQuery(String tableName, String column) {
		return "SELECT MAX(" + column + ") FROM " + tableName;
	}

	@Override
	public void setOrderBy(String name, String desc) {
		this._orderBy = name;
		this._orderDirection = desc;
	}

	@Override
	public void clear() {
		this._list.clear();
		this._conditionList.clear();
		this._tableList.clear();
		this._leftJoinList.clear();
		this._rowList.clear();
		_orderBy = "";
	}
	private String format(String query) {
		if (query == null) return query;
		query = query.replaceAll("\\\\", "\\\\\\\\");
		query = query.replaceAll("\"", "\\\"");
		query = query.replaceAll("'", "\\\\\'");  //  ' -> \'
		return query;
	}
	public String getCrossJoins() {
		StringBuffer crossJoins = new StringBuffer();
		
		Iterator<String> iter = _crossJoinList.iterator();
		
		if (_crossJoinList.size() == 0) {
			return crossJoins.toString();
		} else {
			crossJoins.append(iter.next());
		}
		while (iter.hasNext()) {
			crossJoins.append(" cross join " + iter.next());
		}
		
		return crossJoins.toString();
	}
	public void addCrossJoin(String tableName, String name1, String name2) {
		this._crossJoinList.add("`" + tableName + "` ON " + name1 + " = " + name2);
	}
	
	public void addCrossJoinMultiCondition(String tableName, String name1, String name2, String name3, String name4) {
		this._crossJoinList.add("`" + tableName + "` ON " + name1 + " = " + name2 + " and " + name3 + " = " + name4);
	}
	
}
