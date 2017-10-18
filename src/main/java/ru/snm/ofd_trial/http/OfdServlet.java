package ru.snm.ofd_trial.http;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * @author snm
 */
public class OfdServlet extends HttpServlet {
    private final static Logger logger = LogManager.getLogger();

    @Override
    protected void doPost( HttpServletRequest req, HttpServletResponse resp )
            throws ServletException, IOException
    {
        String xml = req.getReader().lines()
                .collect( Collectors.joining( System.lineSeparator() ) );
    }
}
