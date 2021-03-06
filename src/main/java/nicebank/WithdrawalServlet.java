/***
 * Excerpted from "The Cucumber for Java Book",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/srjcuc for more book information.
***/
package nicebank;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WithdrawalServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -462283587125978632L;
	private CashSlot cashSlot;
	private Account account;
	private String reasonForNoWithdrawal = "";

	public WithdrawalServlet(CashSlot cashSlot, Account account) {
		this.cashSlot = cashSlot;
		this.account = account;
	}

	public String notDispensedReason() {
		return reasonForNoWithdrawal;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Teller teller = new AutomatedTeller(cashSlot);
		int amount = Integer.parseInt(request.getParameter("amount"));
		try {
			teller.withdrawFrom(account, amount);
		} catch (InsufficientFundsException e) {
			reasonForNoWithdrawal = e.getReason();
		}

		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);

		if (reasonForNoWithdrawal == "") {
			response.getWriter().println(
					"<html><head><title>ATM</title></head>" + "<body>Please take your $" + amount + "</body></html>");
		} else {
			response.getWriter().println(
					"<html><head><title>ATM</title></head>" + "<body>" + reasonForNoWithdrawal + "</body></html>");
		}
	}
}