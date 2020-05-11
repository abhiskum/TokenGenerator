package org.gs.token.resource;

import org.gs.token.model.Token;
import org.gs.token.model.TokenRequest;
import org.gs.token.model.ItemType;
import org.gs.token.service.TokenService;
import org.jboss.resteasy.annotations.Form;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("{clientId}/tokens")
public class TokenResource {

    private TokenService tokenService;

    @Inject
    public TokenResource(TokenService tokenService){
        this.tokenService = tokenService;
    }


    @POST
    @Produces(MediaType.TEXT_HTML)
    public String createToken(@PathParam("clientId") String clientId,
       @Form TokenRequest tokenRequest) {

        Token token = tokenService.getToken(clientId, tokenRequest);

        StringBuilder htmlResponse = new StringBuilder();
        htmlResponse.append("<html>");
        htmlResponse.append("<body>");
        htmlResponse.append("<h2>").append(token.getRequesterName()).append(", your ").append(token.getItemType().getDisplayName())
            .append(" token generated successfully</h2>");
        htmlResponse.append("<table border=\"1\">");

        htmlResponse.append("<tr>").append("<td>Token Number</td>").append("<td>")
            .append(token.getTokenNumber()).append("</td>").append("</tr>");
        htmlResponse.append("<tr>").append("<td>Slot Timing</td>").append("<td>")
            .append(formattedDate(token.getSlotStartTiming())).append("-").append(formattedDate(token.getSlotEndTiming())).append("</td>").append("</tr>");
        htmlResponse.append("<tr>").append("<td>Created Date</td>").append("<td>")
            .append(formattedDate(token.getCreatedDate())).append("</td>").append("</tr>");

        htmlResponse.append("</table>");
        htmlResponse.append("</body>");
        htmlResponse.append("</html>");
        return htmlResponse.toString();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/{item}/list")
    public String listToken(@PathParam("clientId") String clientId, @PathParam("item") String item) {

        ItemType itemType = tokenService.getItemType(clientId, item);
        List<Token> tokenList = itemType.getGeneratedTokens();
        StringBuilder htmlResponse = new StringBuilder();
        htmlResponse.append("<html>");
        htmlResponse.append("<body>");
        htmlResponse.append("<h2>").append(itemType.getDisplayName()).append(" Tokens</h2>");
        htmlResponse.append("<table border=\"1\">");
        htmlResponse.append("<tr>");
        htmlResponse.append("<td>Token Number</td>");
        htmlResponse.append("<td>Name</td>");
        htmlResponse.append("<td>FlatNumber</td>");
        htmlResponse.append("<td>Mobile</td>");
        htmlResponse.append("<td>Slot Timing</td>");
        htmlResponse.append("<td>Created Date</td>");
        htmlResponse.append("</tr>");
        tokenList.forEach(token -> {
            htmlResponse.append("<tr>");
            htmlResponse.append("<td>").append(token.getTokenNumber()).append("</td>");
            htmlResponse.append("<td>").append(token.getRequesterName()).append("</td>");
            htmlResponse.append("<td>").append(token.getRequesterFlatNumber()).append("</td>");
            htmlResponse.append("<td>").append(token.getRequesterMobile()).append("</td>");
            htmlResponse.append("<td>").append(formattedDate(token.getSlotStartTiming())).append("-").append(formattedDate(token.getSlotEndTiming())).append(
                "</td>");
            htmlResponse.append("<td>").append(formattedDate(token.getCreatedDate())).append("</td>");
            htmlResponse.append("</tr>");
        });
        htmlResponse.append("</table>");
        htmlResponse.append("</body>");
        htmlResponse.append("</html>");
        return htmlResponse.toString();
    }

    /**
     *
     * @param date
     * @return Return formatted date
     */
    private String formattedDate(Date date){
        return date.toString();
    }
}