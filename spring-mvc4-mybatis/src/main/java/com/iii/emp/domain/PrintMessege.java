package com.iii.emp.domain;

import java.io.Serializable;
import java.util.Date;

public class PrintMessege implements Serializable {

	private static final long serialVersionUID = 1L;

	public String getDate() {
		return "getData(" + new Date() + ")";
	}

}
