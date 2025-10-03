<template>
  <ProfileDetail v-if="user" :user="user"></ProfileDetail>
</template>

<script>
import { getStore } from "@/common/store";
import UserRepository from "@/repositories/UserRepository";
import ProfileDetail from "@/components/profile/ProfileDetail.vue";

export default {
  name: "ProfileDetailView",
  data() {
    return {
      user: null,
    };
  },
  components: { ProfileDetail },
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
