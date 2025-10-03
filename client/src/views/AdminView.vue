<template>
  <AdminDetail v-if="user" :user="user"></AdminDetail>
</template>

<script>
import { getStore } from "@/common/store";
import UserRepository from "@/repositories/UserRepository";
import AdminDetail from "@/components/admin/AdminDetail";

export default {
  name: "AdminView",
  data() {
    return {
      user: null,
    };
  },
  components: { AdminDetail },
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
          this.user = await UserRepository.findOne(getStore().state.user.id);
        } catch (err) {
          console.error(err);
        }
      }
    },
  },
};
</script>

<style scoped></style>
