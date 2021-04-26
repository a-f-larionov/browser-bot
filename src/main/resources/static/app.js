Vue.component("login-form", {

    template: "#templateLoginForm",

    data: function () {
        return {
            password: ""
        };
    },

    methods: {
        loginDo: function () {

            axios.post("/login", {
                password: this.password

            }).then(function (answer) {
                eventBus.$emit(eventBus.EVENT_CHECK_AUTH);

            }).catch(function () {

                alert("Что то пошло не так. Обратитесь к разработчику.");
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
            axios.get("/get_manager_list")
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

            axios.get("/manager_working_switch?managerId=" + self.manager.id, {
                managerId: self.manager.id
            }).then(function () {

                eventBus.$emit(eventBus.EVENT_REQUEST_MANAGER_UPDATE);

            }).catch(function () {
                alert("ERR 85");
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

        eventBus.$on(eventBus.EVENT_CHECK_AUTH, this.updateAuthorization);
    },
    mounted: function () {

        eventBus.$emit(eventBus.EVENT_CHECK_AUTH);
    },

    data: {
        loginFormShow: false,
        controlFormShow: false,
    },

    methods: {
        updateAuthorization: function () {
            let self = this;
            axios.get("is_it_auth")
                .then(function (answer) {

                    if (answer.data.isIt) {
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