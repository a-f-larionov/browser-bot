//@Todo html-stylized-notification
var catchRequest = function (error) {
    if (error.response && error.response.status && error.response.data &&
        error.response.status == 400) {

        alert(error.response.data);

    } else {
        alert("Что то пошло не так. Обратитесь к разработчику.");
    }
};

Vue.component("login-form", {

    template: "#templateLoginForm",

    data: function () {
        return {
            password: "",
            username: ""
        };
    },

    methods: {
        loginDo: function () {

            var params = new URLSearchParams();
            //@todo username is default
            params.append('username', this.username);
            params.append('password', this.password);

            var config = {
                headers: {
                    'Content-type': 'application/x-www-form-urlencoded'
                }
            };

            axios.post("/authorize", params, config).then(function (answer) {
                console.log(answer);
                eventBus.$emit(eventBus.EVENT_CHECK_AUTH);

            }).catch(function (error) {

                catchRequest(error);
            });
        }
    }
});

Vue.component("register-form", {

    template: "#templateRegisterUser",

    data: function () {
        return {
            username: "",
            password: ""
        }
    },

    created: function () {

    },
    methods: {
        registerDo: function () {

            axios.post("/admin/register-user", {
                username: this.username,
                password: this.password
            }).then(function (answer) {
                //@todo
            }).catch(function (error) {
                catchRequest(error);
            });
        }
    }
});

Vue.component("control-form", {

    template: "#templateControlForm",

    data: function () {
        return {
            "managers": []
        };
    },

    created: function () {
        let self = this;
        eventBus.$on(eventBus.EVENT_REQUEST_MANAGER_UPDATE, function () {
            axios.get("/managers/list")
                .then(function (answer) {
                    eventBus.$emit(eventBus.EVENT_UPDATE_MANAGER_LIST, answer.data);
                })
                .catch(function () {
                    alert("ERR: 45");
                })
        })
        eventBus.$on(eventBus.EVENT_UPDATE_MANAGER_LIST, function (data) {
            self.managers = data;
        });
    },
    mounted: function () {
        let self = this;

        eventBus.$emit(eventBus.EVENT_REQUEST_MANAGER_UPDATE);
    },

    methods: {}
});

Vue.component("manager-item", {
    template: "#templateManagerItem",
    props: ['manager'],
    data: function () {
        return {isWorking: false};
    },
    created: function () {
        this.isWorking = this.manager.isWorking;
    },
    methods: {
        onWorkingchange: function () {
            let self = this;

            let params = new URLSearchParams();
            params.append("managerId", self.manager.id);

            axios.post("/managers/works-switch", params)
                .then(function () {

                    eventBus.$emit(eventBus.EVENT_REQUEST_MANAGER_UPDATE);

                }).catch(function (error) {
                catchRequest(error);
            })
        },
    }
});

let eventBus = new Vue();
eventBus.EVENT_REQUEST_MANAGER_UPDATE = "requestManagerUpate";
eventBus.EVENT_UPDATE_MANAGER_LIST = "updateManagerList";
eventBus.EVENT_CHECK_AUTH = "CheckAuth";

let app = new Vue({

    el: "#vueapp",

    created: function () {

        eventBus.$on(eventBus.EVENT_CHECK_AUTH, this.updateUserProfile);
    },
    mounted: function () {

        eventBus.$emit(eventBus.EVENT_CHECK_AUTH);
    },

    data: {
        loginFormShow: false,
        controlFormShow: false,
        userProfile: null,
    },

    methods: {
        updateUserProfile: function () {
            let self = this;
            axios.get("/users/get")
                .then(function (answer) {

                    self.userProfile = answer.data;

                    if (self.userProfile.id) {
                        self.loginFormShow = false;
                        self.controlFormShow = true;
                    } else {

                        self.loginFormShow = true;
                        self.controlFormShow = false;
                    }

                }).catch(function () {
                console.debug(arguments);
                alert("Не могу определить авторизацию, Обратитесь к разработчику.");
            })
        }
    }
});