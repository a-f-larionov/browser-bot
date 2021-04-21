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

    methods: {
        testButton: () => {
            axios.get("/test")
                .then(function (answer) {
                    alert("шикарно");
                })
                .catch(function () {
                    alert("Что то не так!");
                });
        }
    }
});


let eventBus = new Vue();
eventBus.EVENT_CHECK_AUTH = "eventCheckAuth";

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