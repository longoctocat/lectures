package ru.naumen.lecture.tooling;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ToolTesterServlet extends HttpServlet
{
    private static final long serialVersionUID = 579366660553979193L;

    private static final String COMMAND_PRM_NAME = "command";

    private final static List<IToolTesterCommand> AVAILABLE_COMMANDS = Arrays.asList(
            new HowAreYouCommand(), new DeadlockCommand(), new LoadCPUCommand(), new EatAllHeapCommand());

    private State currentState = State.ALL_RIGHT;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        //FIXME: вызывается 2 раза
        String command = request.getParameter(COMMAND_PRM_NAME);
        Optional<IToolTesterCommand> cmd = AVAILABLE_COMMANDS.stream().filter(c -> c.getCode().equals(command)).findFirst();

        //В web-приложении так делать нельзя. Только для примера
        synchronized (this)
        {
            if (cmd.isPresent() && canExecute(cmd.get().getTargetState()))
            {
                currentState = cmd.get().getTargetState();
                cmd.get().execute();
            }
        }

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("<h1>Состояние: " + (currentState == null ? "Не определено" : currentState.getTitle()) + "</h1>");

        final StringBuilder form = new StringBuilder();
        form.append("<form action=\"/\">");
        AVAILABLE_COMMANDS.forEach(c -> {
            form.append("<button name=\"" + COMMAND_PRM_NAME + "\" value = \"" + c.getCode() + "\"" + (currentState.isError() && c.getTargetState().isError() ? " disabled " : "") + ">" + c.getName() + "</button>");
        });

        form.append("</form>");
        response.getWriter().println(form);
    }

    private boolean canExecute(State targetState)
    {
        return !State.SAME_AS_CURRENT.equals(targetState) && (!targetState.isError() || !currentState.isError());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        doGet(req, resp);
    }
}