<template>
  <UserEdit v-if="user" :user="user"></UserEdit>
</template>

<script>
import UserEdit from "@/components/users/UserEdit";
import { getStore } from "@/common/store";
import UserRepository from "@/repositories/UserRepository";

export default {
  name: "UserEditView",
  data() {
    return {
      user: null,
    };
  },
  components: { UserEdit },
  mounted() {
    this.fetchData();
  },
  computed: {
    isLogged() {
      return getStore().state.user.logged;
    },
  },
  methods: {
    async fetchData() {
      if (this.isLogged) {
        try {
          const id = this.$route.params.id;
          this.user = await UserRepository.findOne(id);
        } catch (err) {
          console.error(err);
        }
      }
    },
  },
};
</script>

<style scoped></style>
