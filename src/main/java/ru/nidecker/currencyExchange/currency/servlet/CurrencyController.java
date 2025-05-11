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

@WebServlet(name = "CurrencyController", urlPatterns = "/api/currency/{code}")
public class CurrencyController extends HttpServlet {
    private final ObjectMapper mapper;
    private final CurrencyService currencyService;

    public CurrencyController() {
        this.mapper = new ObjectMapper();
        this.currencyService = new CurrencyServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        String code;
        if (pathInfo != null && pathInfo.length() > 1) {
            code = pathInfo.substring(1);
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(resp.getWriter(), new ExceptionResponse("Код валюты отсутствует в адресе"));
            return;
        }

        try {
            CurrencyResponse currency = currencyService.findByCode(code);
            mapper.writeValue(resp.getWriter(), currency);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
