package ru.nidecker.currencyExchange.currency.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.nidecker.currencyExchange.currency.CurrencyResponse;
import ru.nidecker.currencyExchange.currency.CurrencyService;
import ru.nidecker.currencyExchange.currency.impl.CurrencyServiceImpl;
import ru.nidecker.currencyExchange.exceptions.ExceptionResponse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "CurrenciesController", urlPatterns = "/api/currencies")
public class CurrenciesController extends HttpServlet {
    private final ObjectMapper mapper;
    private final CurrencyService currencyService;

    public CurrenciesController() {
        this.mapper = new ObjectMapper();
        this.currencyService = new CurrencyServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<CurrencyResponse> list = currencyService.findAll();
            resp.setStatus(HttpServletResponse.SC_OK);
            mapper.writeValue(resp.getWriter(), list);
        } catch (Exception e) {
            mapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
