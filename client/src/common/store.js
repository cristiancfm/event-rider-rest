import { ref } from "vue";

const store = ref({
  state: {
    user: {
      id: "",
      authority: "",
      email: "",
      logged: false,
    },
  },
});

export { getStore };

function getStore() {
  return store.value;
}
