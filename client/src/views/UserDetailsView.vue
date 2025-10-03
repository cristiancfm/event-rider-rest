<template>
  <UserDetails
    v-if="user"
    :user="user"
    @followers="updateFollowers"
  ></UserDetails>
</template>

<script>
import UserDetails from "@/components/users/UserDetails.vue";
import UserRepository from "@/repositories/UserRepository";
import { updateFollowers } from "@/common/user";

export default {
  name: "UserDetailsView",
  data() {
    return {
      user: null,
    };
  },
  components: { UserDetails },
  mounted() {
    this.fetchData();
  },
  methods: {
    async fetchData() {
      try {
        this.user = await UserRepository.findOne(this.$route.params.id);
      } catch (err) {
        console.error(err);
      }
    },
    updateFollowers,
  },
};
</script>

<style scoped></style>
