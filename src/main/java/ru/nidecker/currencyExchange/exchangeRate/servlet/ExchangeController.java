package ru.nidecker.currencyExchange.exchangeRate.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.nidecker.currencyExchange.exceptions.ExceptionResponse;
import ru.nidecker.currencyExchange.exceptions.InvalidParameterException;
import ru.nidecker.currencyExchange.exceptions.NotFoundException;
import ru.nidecker.currencyExchange.exchangeRate.ExchangeRateService;
import ru.nidecker.currencyExchange.exchangeRate.entity.ExchangeResponse;
import ru.nidecker.currencyExchange.exchangeRate.impl.ExchangeRateServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet(name = "ExchangeController", urlPatterns = "/api/exchange")
public class ExchangeController extends HttpServlet {
    private final ObjectMapper mapper;
    private final ExchangeRateService service;

    public ExchangeController() {
        this.mapper = new ObjectMapper();
        this.service = new ExchangeRateServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            ExchangeResponse exchangeResponse = service.calculateExchange(
                    req.getParameter("from"),
                    req.getParameter("to"),
                    req.getParameter("amount")
            );
            mapper.writeValue(resp.getWriter(), exchangeResponse);
        } catch (InvalidParameterException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        } catch (NotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            mapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            mapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        }
    }
}
