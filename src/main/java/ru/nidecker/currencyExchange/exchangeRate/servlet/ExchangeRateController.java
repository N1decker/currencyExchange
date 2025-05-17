package ru.nidecker.currencyExchange.exchangeRate.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.nidecker.currencyExchange.exceptions.ExceptionResponse;
import ru.nidecker.currencyExchange.exceptions.NotFoundException;
import ru.nidecker.currencyExchange.exchangeRate.ExchangeRateResponse;
import ru.nidecker.currencyExchange.exchangeRate.ExchangeRateService;
import ru.nidecker.currencyExchange.exchangeRate.impl.ExchangeRateServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet(name = "ExchangeRateController", urlPatterns = "/api/exchangeRate/*")
public class ExchangeRateController extends HttpServlet {
    private final ObjectMapper mapper;
    private final ExchangeRateService service;
    private static final Integer CURRENCY_CODES_PAIR_LENGTH = 6;

    public ExchangeRateController() {
        this.mapper = new ObjectMapper();
        this.service = new ExchangeRateServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        String pair, code1, code2;
        if (pathInfo.length() == CURRENCY_CODES_PAIR_LENGTH + 1) {
            pair = pathInfo.substring(1);
            code1 = pair.substring(0, CURRENCY_CODES_PAIR_LENGTH / 2);
            code2 = pair.substring(CURRENCY_CODES_PAIR_LENGTH / 2);

        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(resp.getWriter(), new ExceptionResponse("Коды валют пары отсутствуют в адресе"));
            return;
        }

        try {
            ExchangeRateResponse response = service.findByPair(code1, code2);
            mapper.writeValue(resp.getWriter(), response);
        } catch (NotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            mapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            mapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getMethod().equalsIgnoreCase("PATCH")) {
            doPatch(req, resp);
        } else {
            super.service(req, resp);
        }
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        String pair, code1, code2;
        if (pathInfo.length() == CURRENCY_CODES_PAIR_LENGTH + 1) {
            pair = pathInfo.substring(1);
            code1 = pair.substring(0, CURRENCY_CODES_PAIR_LENGTH / 2);
            code2 = pair.substring(CURRENCY_CODES_PAIR_LENGTH / 2);

        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(resp.getWriter(), new ExceptionResponse("Коды валют пары отсутствуют в адресе"));
            return;
        }

        String line = req.getReader().readLine();
        String[] params = line.split("&");
        BigDecimal rate = null;
        for (String param : params) {
            if (param.contains("rate")) {
                rate = BigDecimal.valueOf(Double.parseDouble(param.substring(param.indexOf("=") + 1)));
            }
        }

        if (rate == null || rate.doubleValue() < 0d) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(resp.getWriter(), new ExceptionResponse("Отсутствует нужное поле формы"));
            return;
        }

        try {
            ExchangeRateResponse response = service.update(code1, code2, rate);
            mapper.writeValue(resp.getWriter(), response);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            mapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        }
    }
}
