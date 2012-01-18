package exception;

public class DataBaseException extends Exception {
	
	public final static String DATABASE_ACCESS_ERROR = "Error accessing the database";
	public final static String DATABASE_CONNECTION_ERROR = "Error connecting to database";

	public DataBaseException() {
	}

	public DataBaseException(String msg) {
		super(msg);
	}

}
