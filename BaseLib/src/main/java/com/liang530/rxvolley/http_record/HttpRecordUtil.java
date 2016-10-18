package com.liang530.rxvolley.http_record;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.util.Date;

import com.liang530.log.L;

/**
 * 
 * @author liang
 * @类   说   明:	地址工具类
 * @version 1.0
 * @创建时间：2015-4-10 上午9:50:34
 */
public class HttpRecordUtil {
	static String TAG="httpRecordUtil";
	static String dbDir; //文件存放所在的路径
	static String DATABASE_NAME = "http_record.db"; //数据库名
//	static String CREATE_TABLE_SQL="CREATE TABLE http_record (id  integer auto_increment primary key, flagJson  text,url  text,params  text,files  text,method  text,listenerName  text,showDialog  int,count  int,type int ,createTime TimeStamp Not null default(datetime('now','localtime')));";
	static String CREATE_TABLE_SQL="CREATE TABLE http_record (id  integer auto_increment primary key, flagJson  text,url  text,params  text,files  text,method  text,listenerName  text,gotoActName  text,showDialog  int,count  int,type int ,createTime INTEGER Not null);";

	private static DataBaseOpenHelper openHelper;
	
	
	public static void init(Context context){
		openHelper = new DataBaseOpenHelper(context.getApplicationContext(),DATABASE_NAME);
		dbDir=context.getDatabasePath(DATABASE_NAME).getParent();
		File dbFile = context.getDatabasePath(DATABASE_NAME);
		dbFile.delete();
		checkDataBase(context);
	}


	/**
	 * 根据省的id建数据库表
	 */
	private static void createDataBase(){
		SQLiteDatabase db =openHelper.getWritableDatabase();
		db.beginTransaction();        //手动设置开始事务
		//创建出表
		db.execSQL(CREATE_TABLE_SQL);
		db.setTransactionSuccessful();
		db.endTransaction(); 
		db.close();
	}

	public static int insert(HttpRecord httpRecord){
		int id=0;
		SQLiteDatabase db =openHelper.getWritableDatabase();
		db.beginTransaction();        //手动设置开始事务
		//实例化常量值
		ContentValues cValue = new ContentValues();
		cValue.put("flagJson",httpRecord.getFlagJson());
		cValue.put("url",httpRecord.getUrl());
		cValue.put("params",httpRecord.getParams());
		cValue.put("files",httpRecord.getFiles());
		cValue.put("method",httpRecord.getMethod());
		cValue.put("listenerName",httpRecord.getListenerName());
		cValue.put("gotoActName",httpRecord.getGotoActName_Position());
		cValue.put("showDialog",httpRecord.getShowDialog());
		cValue.put("count",httpRecord.getCount());
		cValue.put("type",httpRecord.getType());
		cValue.put("createTime",new Date().getTime());
		//调用insert()方法插入数据
		db.insert("http_record",null,cValue);
		//获得刚插入的id
		Cursor cursor = db.rawQuery("select last_insert_rowid() id", null);
		if(cursor.moveToNext()){
			id=cursor.getInt(cursor.getColumnIndex("id"));
		}
		cursor.close();
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
		return id;
	}
	public static HttpRecord getByFlagJsonNew(String flagJson){
		SQLiteDatabase db =openHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from http_record where flagJson= '"+flagJson+"' order by createTime desc limit 1", null);
		HttpRecord httpRecord=null;
		while(cursor.moveToNext()){
			httpRecord=new HttpRecord();
			httpRecord.setId(cursor.getLong(cursor.getColumnIndex("id")));
			httpRecord.setFlagJson(cursor.getString(cursor.getColumnIndex("flagJson")));
			httpRecord.setUrl(cursor.getString(cursor.getColumnIndex("url")));
			httpRecord.setCreateTime(cursor.getLong(cursor.getColumnIndex("createTime")));
		}
		cursor.close();
		return httpRecord;
	}
	public static HttpRecord getById(int id){
		SQLiteDatabase db =openHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from http_record where id= "+id+" order by createTime desc", null);
		HttpRecord httpRecord=null;
		while(cursor.moveToNext()){
			httpRecord=new HttpRecord();
			httpRecord.setId(cursor.getInt(cursor.getColumnIndex("id")));
			httpRecord.setFlagJson(cursor.getString(cursor.getColumnIndex("flagJson")));
			httpRecord.setUrl(cursor.getString(cursor.getColumnIndex("url")));
			httpRecord.setCreateTime(cursor.getLong(cursor.getColumnIndex("createTime")));
		}
		cursor.close();
		return httpRecord;
	}

//	private void saveToDataBase(SQLiteDatabase db,int tableId,List<RecodeEntity> data){
//		StringBuffer buffer=null;
//		for (RecodeEntity recodeEntity : data) {
//			buffer=new StringBuffer("insert into province");
//			buffer.append(tableId);
//			buffer.append("(id,areaname,parentid,level)values(");
//			buffer.append(recodeEntity.id+",");
//			buffer.append("'"+recodeEntity.areaname+"',");
//			buffer.append(recodeEntity.parentid+",");
//			buffer.append(recodeEntity.level+")");
////			L.e("insert", "insert"+buffer.toString());
//			db.execSQL(buffer.toString());
//		}
//	}


	/**
	 * 根据表名和所在层获取所有省的数据
	 * @param tableId 表名id
	 * @param level //所在层级
	 * @return
//	 */
//	public List<RecodeEntity> getProvinceData(int tableId,int level){
//		SQLiteDatabase db =openHelper.getReadableDatabase();
//		String sql="select * from "+TABLE_PRE+tableId+" where level="+level;
//		L.e("hongliang", "getProvinceData:"+sql);
//		Cursor cursor = db.rawQuery(sql, null);
//		List<RecodeEntity> data=new ArrayList<RecodeEntity>();
//		RecodeEntity entity=null;
//		while(cursor.moveToNext()){
//			entity=new RecodeEntity();
//			entity.id=cursor.getInt(cursor.getColumnIndex("id"));
//			entity.areaname=cursor.getString(cursor.getColumnIndex("areaname"));
//			entity.parentid=cursor.getInt(cursor.getColumnIndex("parentid"));
//			entity.level=level;
//			data.add(entity);
//		}
//		return data;
//	}
	/**
	 * 检查数据库是否存在，如不存在，则创建一个
	 */
	private static void checkDataBase(Context context) {
		L.e(TAG, "checkDataBase数据是否存在");
		File dbFile = context.getDatabasePath(DATABASE_NAME);
		if (!dbFile.exists()) {
			createDataBase();
		}else {
			L.e(TAG, "数据库文件存在："+context.getDatabasePath(DATABASE_NAME).length());
		}

	}

	/**
	 * 数据库使用
	 * @author liang
	 *
	 */
	public static class DataBaseOpenHelper extends SQLiteOpenHelper {
		public DataBaseOpenHelper(Context context,String dataBaseName) {
			super(context, dataBaseName, null, 1);
		}
		@Override
		public void onCreate(SQLiteDatabase db) {
		}
		@Override
		public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
			
		}
	}
}
