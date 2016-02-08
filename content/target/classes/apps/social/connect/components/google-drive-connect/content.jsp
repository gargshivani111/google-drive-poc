<%@include file="/libs/foundation/global.jsp" %>
<%@page session="false" %>

Google Drive Connect component.
<%
    String responseCode = request.getParameter("code");
%>
<input type="submit" value="click here" id="b1"/>
<input type="hidden" value="<%=responseCode%>" id="code"/>
<script>
    $(document).ready(function () {
        $("#b1").click(function () {
            window.location = "https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/drive&state=security_token%3D138r5719ru3e1%26url%3Dhttps://oa2cb.example.com/myHome&redirect_uri=http://localhost:7502/etc/cloudservices/googledriveconnect/GoogleDriveDemo.html&response_type=code&client_id=260020511067-04b4a7gqd3brujo5slvss6tfpam57oac.apps.googleusercontent.com";
            console.log();
        });
        var code = $('#code').val();
        var token;
        $.ajax(
                {
                    url: "/bin/googleDriveToken",
                    type: 'post',
                    data: {
                        'code': code
                    },
                    success: function (data) {
                        console.log("...........data is"+data);




                    }
                });
    });
</script>
