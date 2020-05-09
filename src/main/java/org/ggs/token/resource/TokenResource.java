package org.ggs.token.resource;

import org.ggs.token.model.Token;
import org.ggs.token.model.TokenType;
import org.ggs.token.service.TokenService;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/tokens")
public class TokenResource {

    private TokenService tokenService = new TokenService();

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("{parentName}/{tokenType}")
    public String token(@PathParam("parentName") String parentName, @PathParam("tokenType") String type) {

        Token token = tokenService.getToken(parentName, type);

        StringBuilder htmlResponse = new StringBuilder();
        htmlResponse.append("<html>");
        htmlResponse.append("<body>");
        htmlResponse.append("<h2>").append(token.getTokenType().getDisplayName())
            .append(" token " + "generated successfully</h2>");
        htmlResponse.append("<table border=\"1\">");

        htmlResponse.append("<tr>").append("<td>Token Number</td>").append("<td>")
            .append(token.getTokenNumber()).append("</td>").append("</tr>");
        htmlResponse.append("<tr>").append("<td>Estimated Time</td>").append("<td>")
            .append(token.getEstimatedDate()).append("</td>").append("</tr>");
        htmlResponse.append("<tr>").append("<td>Created Date</td>").append("<td>")
            .append(token.getCreatedDate()).append("</td>").append("</tr>");

        htmlResponse.append("</table>");
        htmlResponse.append("</body>");
        htmlResponse.append("</html>");
        return htmlResponse.toString();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("{parentName}/{tokenType}/list")
    public String listToken(@PathParam("parentName") String parentName, @PathParam("tokenType") String type) {

        TokenType tokenType = tokenService.getTokenType(parentName, type);
        List<Token> tokenList = tokenService.getAllTokens(parentName, type);
        StringBuilder htmlResponse = new StringBuilder();
        htmlResponse.append("<html>");
        htmlResponse.append("<body>");
        htmlResponse.append("<h2>").append(tokenType.getDisplayName()).append(" Tokens</h2>");
        htmlResponse.append("<table border=\"1\">");
        htmlResponse.append("<tr>");
        htmlResponse.append("<td>Token Number</td>");
        htmlResponse.append("<td>Estimated Time</td>");
        htmlResponse.append("<td>Created Date</td>");
        htmlResponse.append("</tr>");
        tokenList.forEach(token -> {
            htmlResponse.append("<tr>");
            htmlResponse.append("<td>").append(token.getTokenNumber()).append("</td>");
            htmlResponse.append("<td>").append(token.getEstimatedDate()).append("</td>");
            htmlResponse.append("<td>").append(token.getCreatedDate()).append("</td>");
            htmlResponse.append("</tr>");
        });
        htmlResponse.append("</table>");
        htmlResponse.append("</body>");
        htmlResponse.append("</html>");
        return htmlResponse.toString();
    }
}