<%@ page contentType="text/html;charset=UTF-8" %>

<div class="settings-pane">

    <a href="#" data-toggle="settings-pane" data-animate="true">
        &times;
    </a>

    <div class="settings-pane-inner">

        <div class="row">

            <div class="col-md-4">

                <div class="user-info">

                    <div class="user-image">
                        <a href="javascript:void(0);">
                            <img src="${ctxStatic}/images/user-2.png" class="img-responsive img-circle" />
                        </a>
                    </div>

                    <div class="user-details">

                        <h3>
                            <a href="extra-profile.html">${sessionScope.sessionInfo.loginName}</a>

                            <span class="user-status is-online"></span>
                        </h3>

                        <p class="user-title">消费贷业务员</p>

                    </div>

                </div>

            </div>

            <div class="col-md-8 link-blocks-env">

                <div class="links-block left-sep">
                    <img src="${ctxStatic}/images/logo@2x.png" width="70" alt=""/>
                </div>

                <div class="links-block left-sep">
                    <img src=""alt=""/>
                </div>
            </div>
        </div>
    </div>
</div>
