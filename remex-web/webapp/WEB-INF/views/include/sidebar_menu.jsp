<%@ page contentType="text/html;charset=UTF-8" %>

<div class="sidebar-menu toggle-others fixed">
    <div class="sidebar-menu-inner">

        <header class="logo-env">

            <!-- logo -->
            <div class="logo">
                <a href="/demo" class="logo-expanded">
                    <img src="${ctxStatic}/images/logo@2x.png" width="35" alt="" />&nbsp;小黑鱼
                </a>

                <a href="/demo" class="logo-collapsed">
                    <img src="${ctxStatic}/images/logo@2x.png" width="35" alt=""/>
                </a>
            </div>

            <!-- This will toggle the mobile menu and will be visible only on mobile devices -->
            <div class="mobile-menu-toggle visible-xs">
                <a href="#" data-toggle="user-info-menu">
                    <i class="fa-bell-o"></i>
                    <span class="badge badge-success">7</span>
                </a>

                <a href="#" data-toggle="mobile-menu">
                    <i class="fa-bars"></i>
                </a>
            </div>

            <!-- This will open the popup with user profile settings, you can use for any purpose, just be creative -->
            <div class="settings-icon">
                <a href="#" data-toggle="settings-pane" data-animate="true">
                    <i class="linecons-cog"></i>
                </a>
            </div>


        </header>



        <ul id="main-menu" class="main-menu">
            <!-- add class "multiple-expanded" to allow multiple submenus to open -->
            <!-- class "auto-inherit-active-class" will automatically add "active" class for parent elements who are marked already with class "active" -->
            <li class="active opened active">
                <a href="dashboard-1.html">
                    <i class="linecons-cog"></i>
                    <span class="title">Dashboard</span>
                </a>
                <ul>
                    <li class="active">
                        <a href="dashboard-1.html">
                            <span class="title">Dashboard 1</span>
                        </a>
                    </li>
                    <li>
                        <a href="dashboard-2.html">
                            <span class="title">Dashboard 2</span>
                        </a>
                    </li>
                    <li>
                        <a href="dashboard-3.html">
                            <span class="title">Dashboard 3</span>
                        </a>
                    </li>
                    <li>
                        <a href="dashboard-4.html">
                            <span class="title">Dashboard 4</span>
                        </a>
                    </li>
                    <li>
                        <a href="skin-generator.html">
                            <span class="title">Skin Generator</span>
                        </a>
                    </li>
                </ul>
            </li>
            <li>
                <a href="layout-variants.html">
                    <i class="linecons-desktop"></i>
                    <span class="title">Layouts</span>
                </a>
                <ul>
                    <li>
                        <a href="layout-variants.html">
                            <span class="title">Layout Variants &amp; API</span>
                        </a>
                    </li>
                    <li>
                        <a href="layout-collapsed-sidebar.html">
                            <span class="title">Collapsed Sidebar</span>
                        </a>
                    </li>
                    <li>
                        <a href="layout-static-sidebar.html">
                            <span class="title">Static Sidebar</span>
                        </a>
                    </li>
                    <li>
                        <a href="layout-horizontal-menu.html">
                            <span class="title">Horizontal Menu</span>
                        </a>
                    </li>
                    <li>
                        <a href="layout-horizontal-plus-sidebar.html">
                            <span class="title">Horizontal &amp; Sidebar Menu</span>
                        </a>
                    </li>
                    <li>
                        <a href="layout-horizontal-menu-click-to-open-subs.html">
                            <span class="title">Horizontal Open On Click</span>
                        </a>
                    </li>
                    <li>
                        <a href="layout-horizontal-menu-min.html">
                            <span class="title">Horizontal Menu Minimal</span>
                        </a>
                    </li>
                    <li>
                        <a href="layout-right-sidebar.html">
                            <span class="title">Right Sidebar</span>
                        </a>
                    </li>
                    <li>
                        <a href="layout-chat-open.html">
                            <span class="title">Chat Open</span>
                        </a>
                    </li>
                    <li>
                        <a href="layout-horizontal-sidebar-menu-collapsed-right.html">
                            <span class="title">Both Menus &amp; Collapsed</span>
                        </a>
                    </li>
                    <li>
                        <a href="layout-boxed.html">
                            <span class="title">Boxed Layout</span>
                        </a>
                    </li>
                    <li>
                        <a href="layout-boxed-horizontal-menu.html">
                            <span class="title">Boxed &amp; Horizontal Menu</span>
                        </a>
                    </li>
                    <li>
                        <a href="http://www.cssmoban.com">
                            <span class="title">weidea.net</span>
                        </a>
                    </li>
                </ul>
            </li>
        </ul>

    </div>
</div>