package org.kevin.support.exception;

/**
 * @author kevin
 */
public class DaoException extends RuntimeException {
	private static final long serialVersionUID = -1004067818388851972L;

	public DaoException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DaoException(String message, Throwable cause,
                        boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public DaoException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public DaoException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public DaoException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
