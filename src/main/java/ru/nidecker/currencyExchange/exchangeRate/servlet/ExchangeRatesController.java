package ru.nidecker.currencyExchange.exchangeRate.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.nidecker.currencyExchange.exceptions.DuplicationException;
import ru.nidecker.currencyExchange.exceptions.ExceptionResponse;
import ru.nidecker.currencyExchange.exceptions.InvalidParameterException;
import ru.nidecker.currencyExchange.exceptions.NotFoundException;
import ru.nidecker.currencyExchange.exchangeRate.entity.ExchangeRateRequest;
import ru.nidecker.currencyExchange.exchangeRate.entity.ExchangeRateResponse;
import ru.nidecker.currencyExchange.exchangeRate.ExchangeRateService;
import ru.nidecker.currencyExchange.exchangeRate.impl.ExchangeRateServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ExchangeRatesController", urlPatterns = "/api/exchangeRates")
public class ExchangeRatesController extends HttpServlet {
    private final ObjectMapper mapper;
    private final ExchangeRateService service;

    public ExchangeRatesController() {
        this.mapper = new ObjectMapper();
        this.service = new ExchangeRateServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<ExchangeRateResponse> list = service.findAll();
            mapper.writeValue(resp.getWriter(), list);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            mapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String line = req.getReader().readLine();
            ExchangeRateRequest exchangeRateRequest = service.parseRequestParams(line);
            ExchangeRateResponse exchangeRateResponse = service.create(exchangeRateRequest);
            mapper.writeValue(resp.getWriter(), exchangeRateResponse);
        } catch (InvalidParameterException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        } catch (NotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
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
