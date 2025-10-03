<template>
  <ProfileEdit v-if="user" :user="user"></ProfileEdit>
</template>

<script>
import { getStore } from "@/common/store";
import ProfileEdit from "@/components/profile/ProfileEdit.vue";
import AccountRepository from "@/repositories/AccountRepository";

export default {
  name: "ProfileEditView",
  data() {
    return {
      user: null,
    };
  },
  components: { ProfileEdit },
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
          this.user = await AccountRepository.getAccount();
        } catch (err) {
          console.error(err);
        }
      }
    },
  },
};
</script>

<style scoped></style>
