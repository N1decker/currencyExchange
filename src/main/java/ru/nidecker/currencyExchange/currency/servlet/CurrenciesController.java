package ru.nidecker.currencyExchange.currency.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.nidecker.currencyExchange.currency.entity.Currency;
import ru.nidecker.currencyExchange.currency.entity.CurrencyResponse;
import ru.nidecker.currencyExchange.currency.CurrencyService;
import ru.nidecker.currencyExchange.currency.impl.CurrencyServiceImpl;
import ru.nidecker.currencyExchange.exceptions.DuplicationException;
import ru.nidecker.currencyExchange.exceptions.ExceptionResponse;
import ru.nidecker.currencyExchange.exceptions.InvalidParameterException;

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
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            mapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String line = req.getReader().readLine();
        try {
            Currency currency = currencyService.parseToCurrency(line);
            CurrencyResponse currencyResponse = currencyService.create(currency);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            mapper.writeValue(resp.getWriter(), currencyResponse);
        } catch (InvalidParameterException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        } catch (DuplicationException e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            mapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            mapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        }
    }
}
