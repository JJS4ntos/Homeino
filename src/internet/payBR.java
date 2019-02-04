package internet;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.LocalDate;

import br.com.uol.pagseguro.domain.Sender;
import br.com.uol.pagseguro.domain.Transaction;
import br.com.uol.pagseguro.domain.checkout.Checkout;
import br.com.uol.pagseguro.enums.Currency;
import br.com.uol.pagseguro.enums.TransactionStatus;
import br.com.uol.pagseguro.exception.PagSeguroServiceException;
import br.com.uol.pagseguro.properties.PagSeguroConfig;
import br.com.uol.pagseguro.service.TransactionSearchService;
import database.LoadVersion;

public class payBR {
	
	public payBR(){
		
	}
	
	/***
	 * Envia o usuário à tela de compra
	 * @param sender
	 * @throws SocketException
	 * @throws UnknownHostException
	 */
	public void payment(Sender sender) throws SocketException, UnknownHostException{
		Checkout checkout= new Checkout();
		checkout.addItem(
				"FULL",
				"LICENCE FULL",
				Integer.valueOf(1),
				new BigDecimal("30.0"),
				new Long(0),
				new BigDecimal(0));
		checkout.setShippingCost(new BigDecimal("0"));
		checkout.setSender(sender);
		checkout.setCurrency(Currency.BRL);
		LoadVersion version= new LoadVersion();
		checkout.setReference("USER"+version.getMAC()+"-"+LocalDate.now());
		try{
			boolean onlyCheckoutCode= true;
			//Registra no sistema e retorna o link para o cara pagar
			String link=checkout.register(PagSeguroConfig.getAccountCredentials(), onlyCheckoutCode);
			//Abro o EI com esse link
			Runtime.getRuntime().exec("cmd.exe /C start iexplorer.exe "+link);
		}catch(PagSeguroServiceException | IOException e){
			e.printStackTrace();
		}
	}
	
	/***
	 * Retorna se foi pago ou não a transação
	 * @param transation_code
	 * @return
	 */
	public static boolean checkPaymentPG(String transation_code){
		Transaction transaction=null;
		boolean result=false;
		try {
			transaction= TransactionSearchService.searchByCode(PagSeguroConfig.getAccountCredentials(), transation_code);
			result=transaction.getStatus().equals(TransactionStatus.PAID);
		}catch (PagSeguroServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
}
