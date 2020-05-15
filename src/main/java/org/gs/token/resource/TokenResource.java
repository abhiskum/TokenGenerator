package org.gs.token.resource;

import org.gs.token.model.ItemType;
import org.gs.token.model.Token;
import org.gs.token.service.TokenService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

@Path("{clientId}/tokens")
public class TokenResource {

    private TokenService tokenService;

    @Inject
    public TokenResource(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{item}/slots")
    public ResponseBuilder getSlotTiming(@PathParam("clientId") String clientId,
        @PathParam("item") String item) {
        Map<Integer, String> slotTimings = tokenService.getItemSlots(clientId, item);
        return Response.ok(slotTimings);
    }

    @POST
    @Produces(MediaType.TEXT_HTML)
    public String createToken(@PathParam("clientId") String clientId,
        MultivaluedMap<String, String> requestForm) {

        Token token = tokenService.getToken(clientId, requestForm);

        MultivaluedMap<String, String> requestDetails = token.getRequestDetails();
        StringBuilder htmlResponse = new StringBuilder();
        htmlResponse.append("<html>");
        htmlResponse.append("<body>");
        htmlResponse.append("<h2>").append(
            requestDetails.getFirst("Name") == null ? "Your " : requestDetails.getFirst("Name") + ", your ")
            .append(token.getItemType().getDisplayName()).append(" token generated successfully</h2>");
        htmlResponse.append("<table border=\"1\">");

        htmlResponse.append("<tr>").append("<td>Token Number</td>").append("<td>")
            .append(token.getTokenNumber()).append("</td>").append("</tr>");
        htmlResponse.append("<tr>").append("<td>Slot Timing</td>").append("<td>")
            .append(formattedDate(token.getSlotStartTiming())).append("-")
            .append(formattedDate(token.getSlotEndTiming())).append("</td>").append("</tr>");
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
    public String listTokens(@PathParam("clientId") String clientId, @PathParam("item") String item) {

        ItemType itemType = tokenService.getItemType(clientId, item);
        List<String> uiFields = itemType.getUiFields();
        List<Token> tokenList = itemType.getGeneratedTokens();
        StringBuilder htmlResponse = new StringBuilder();
        htmlResponse.append("<html>");
        htmlResponse.append("<body>");
        htmlResponse.append("<h2>").append(itemType.getDisplayName()).append(" Tokens</h2>");
        htmlResponse.append("<table border=\"1\">");
        htmlResponse.append("<tr>");
        htmlResponse.append("<td>Token Number</td>");
        uiFields.forEach(uiField -> {
            htmlResponse.append("<td>").append(uiField).append("</td>");
        });
        htmlResponse.append("<td>Slot Timing</td>");
        htmlResponse.append("<td>Created Date</td>");
        htmlResponse.append("</tr>");
        tokenList.forEach(token -> {
            htmlResponse.append("<tr>");
            htmlResponse.append("<td>").append(token.getTokenNumber()).append("</td>");
            uiFields.forEach(uiField -> {
                htmlResponse.append("<td>").append(token.getRequestDetails().getFirst(uiField))
                    .append("</td>");
            });
            htmlResponse.append("<td>").append(formattedDate(token.getSlotStartTiming())).append("-")
                .append(formattedDate(token.getSlotEndTiming())).append("</td>");
            htmlResponse.append("<td>").append(formattedDate(token.getCreatedDate())).append("</td>");
            htmlResponse.append("</tr>");
        });
        htmlResponse.append("</table>");
        htmlResponse.append("</body>");
        htmlResponse.append("</html>");
        return htmlResponse.toString();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/list")
    public String listAllTokens(@PathParam("clientId") String clientId) {

        Collection<ItemType> itemTypes = tokenService.getItemTypes(clientId);
        List<Token> tokenList = new ArrayList<>();
        List<String> uiFields = new ArrayList<>();

        itemTypes.stream().forEach(itemType -> {
            tokenList.addAll(itemType.getGeneratedTokens());
            uiFields.addAll(itemType.getUiFields());
        });

        StringBuilder htmlResponse = new StringBuilder();
        htmlResponse.append("<html>");
        htmlResponse.append("<body>");
        htmlResponse.append("<h2>").append("All Tokens</h2>");
        htmlResponse.append("<table border=\"1\">");
        htmlResponse.append("<tr>");
        htmlResponse.append("<td>Token Type</td>");
        htmlResponse.append("<td>Token Number</td>");
        uiFields.forEach(uiField -> {
            htmlResponse.append("<td>").append(uiField).append("</td>");
        });
        htmlResponse.append("<td>Slot Timing</td>");
        htmlResponse.append("<td>Created Date</td>");
        htmlResponse.append("</tr>");
        tokenList.forEach(token -> {
            htmlResponse.append("<tr>");
            htmlResponse.append("<td>").append(token.getItemType().getDisplayName()).append("</td>");
            htmlResponse.append("<td>").append(token.getTokenNumber()).append("</td>");
            uiFields.forEach(uiField -> {
                htmlResponse.append("<td>").append(token.getRequestDetails().getFirst(uiField))
                    .append("</td>");
            });
            htmlResponse.append("<td>").append(formattedDate(token.getSlotStartTiming())).append("-")
                .append(formattedDate(token.getSlotEndTiming())).append("</td>");
            htmlResponse.append("<td>").append(formattedDate(token.getCreatedDate())).append("</td>");
            htmlResponse.append("</tr>");
        });
        htmlResponse.append("</table>");
        htmlResponse.append("</body>");
        htmlResponse.append("</html>");
        return htmlResponse.toString();
    }

    /**
     * @param date
     * @return Return formatted date
     */
    private String formattedDate(Date date) {
        return date.toString();
    }
}