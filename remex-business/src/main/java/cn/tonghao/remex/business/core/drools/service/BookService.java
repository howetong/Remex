package cn.tonghao.remex.business.core.drools.service;

import cn.tonghao.remex.business.core.drools.dto.Book;
import org.kie.api.cdi.KSession;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class BookService {

	@KSession("bookprice_ksession")
	private KieSession priceKsession;

	@KSession("booksupplier_ksession")
	private KieSession supplierKsession;

	/**
	 * 获取一本书的当前实际售价
	 * 
	 * @param b
	 * @return
	 */
	public double getBookSalePrice(Book b) {
		if (b == null) {
			throw new NullPointerException("Book can not be null.");
		}

		priceKsession.insert(b);
		priceKsession.fireAllRules();
		return b.getSalesPrice();
	}

	public Set<String> selectBookSupplier(Book b) {
		if (b == null) {
			throw new NullPointerException("Book can not be null.");
		}
		Set<String> supplierSet = new HashSet<>();
		supplierKsession.setGlobal("supplierSet", supplierSet);
		supplierKsession.insert(b);
		supplierKsession.fireAllRules();
		return b.getSupplier();
	}
}
