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

                console.log(answer.data);

                eventBus.$emit(eventBus.EVENT_CHECK_AUTH);
                //@todo
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
                    console.log(answer.data);
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

                    console.log(answer);
                    console.log(answer.data);
                    console.log(answer.data.isIt);
                    if (answer.data.isIt) {
                        console.log(1);
                        self.loginFormShow = false;
                        self.controlFormShow = true;
                    } else {
                        console.log(2);
                        self.loginFormShow = true;
                        self.controlFormShow = false;
                    }

                }).catch(function () {
                console.log(arguments);
                alert("Не могу определить авторизацию, Обратитесь к разработчику.");
            })
        }
    }
});